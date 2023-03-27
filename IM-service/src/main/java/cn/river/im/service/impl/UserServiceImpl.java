package cn.river.im.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.jwt.JWTUtil;
import cn.river.im.constants.IMConstants;
import cn.river.im.dto.LoginDto;
import cn.river.im.dto.RegisterDto;
import cn.river.im.entity.User;
import cn.river.im.enums.ApiCode;
import cn.river.im.exception.BusinessException;
import cn.river.im.exception.SystemException;
import cn.river.im.mapper.UserMapper;
import cn.river.im.result.Result;
import cn.river.im.service.UserService;
import cn.river.im.util.RedisUtils;
import cn.river.im.vo.SingleChatMsgVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
* @author 阳名
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2022-11-03 15:28:23
*/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final UserMapper userMapper;

    private final RedisUtils redisUtils;

    @Value("${cache-prefix.register-key}")
    private String keyPrefix;

    @Value("${jwt.online}")
    private String onlineKey;

    /*登录*/
    @Override
    public Result<String> login(LoginDto loginDto) {
        //将密码加密
        String md5Hex = DigestUtil.md5Hex(loginDto.getPassWord());
        //查询数据库
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccountNumber,loginDto.getAccountNumber()).eq(User::getPassWord,md5Hex);
        User user = userMapper.selectOne(wrapper);
        if(ObjectUtil.isEmpty(user)){
            return Result.fail(ApiCode.LOGIN_EXCEPTION.getCode(),"账号或密码错误");
        }
        //根据uid删除redis
        redisUtils.likeDel(IMConstants.IM_REDIS_ONLINE_TOKEN + user.getId());
        //从websocket中删除
        //创建token
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid",user.getId());
        map.put("email",user.getAccountNumber());
        map.put("creatTime",System.currentTimeMillis());
        String token = JWTUtil.createToken(map, onlineKey.getBytes());
        //存入redis
        redisUtils.set(IMConstants.IM_REDIS_ONLINE_TOKEN+user.getId()+":"+token,user,3L,TimeUnit.DAYS);
        //将token响应给前端
        return Result.ok(ApiCode.SUCCESS.getCode(), "登录成功",token);
    }

    /**
     * 注册
     * @param registerDto
     * @return
     */
    @Override
    @Transactional
    public Result<String> register(RegisterDto registerDto) {
        try {
            //查询账号是否已注册
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getAccountNumber,registerDto.getAccountNumber());
            User selectOne = userMapper.selectOne(wrapper);
            if(ObjectUtil.isNotEmpty(selectOne)){
                //存在，返回注册失败
                return Result.fail(ApiCode.FAIL.getCode(), "该邮箱已被注册");
            }
            //验证验证码
            String verificationCode = this.verificationCode(registerDto.getCode(), registerDto.getAccountNumber());
            if(verificationCode!=null){
                return Result.fail(ApiCode.FAIL.getCode(), verificationCode);
            }
            User user = new User();
            //bean复制
            BeanUtil.copyProperties(registerDto,user);
            //继续注册
            //加密密码
            String md5Hex = DigestUtil.md5Hex(user.getPassWord());
            user.setPassWord(md5Hex);
            //设置注册时间
            user.setCreationTime(DateUtil.date());
            //设置权限为普通用户
            user.setPower(0);
            //插入数据库
            userMapper.insert(user);
        }catch (Exception e){
            throw new SystemException(ApiCode.REGISTER_EXCEPTION.getCode(),
                    ApiCode.REGISTER_EXCEPTION.getMessage());
        }
        return Result.ok(ApiCode.SUCCESS.getCode(), "注册成功");
    }

    /**
     * 获取验证码
     * @param accountNumber
     * @return
     */
    @Override
    public Result<String> getVerificationCode(String accountNumber) {
        //校验邮箱格式是否正确
        boolean match = ReUtil.isMatch("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", accountNumber);
        if(!match){
            return Result.fail(ApiCode.FAIL.getCode(), "邮箱格式不正确");
        }

        //查询账号是否已注册
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccountNumber,accountNumber);
        User selectOne = userMapper.selectOne(wrapper);
        if(ObjectUtil.isNotEmpty(selectOne)){
            //存在，返回注册失败
            return Result.fail(ApiCode.FAIL.getCode(), "该邮箱已被注册");
        }
        //判断是否发送过验证码
        String s = redisUtils.get(keyPrefix+":"+accountNumber);
        if(StrUtil.isNotEmpty(s)){
            return Result.fail(ApiCode.FAIL.getCode(), "验证码十分钟内有效，请勿重复发送");
        }
        //生成验证码
        String code = RandomUtil.randomString(6);
        log.info("验证码：{}",code);
        //将验证码存入缓存
        redisUtils.set(keyPrefix+":"+accountNumber,code,10L, TimeUnit.MINUTES);
        //邮件标题
        String title="RiverIM邮箱验证码";
        //正文
        String text="<div style='margin-left:30vw'>您的邮箱验证码为：</div> <h1 style='color:#ff7800;text-align: center;'>"+code+"</h1>  <div style='color:red;text-align: center;font-size:12px'>该验证码 10 分钟内有效。为了保障您的账户安全，请勿向他人泄漏验证码信息。</div>";
        //发送邮件
        MailUtil.send(accountNumber,title,text,true);
        return Result.fail(ApiCode.SUCCESS.getCode(), "邮件发送成功，请输入邮箱内验证码");
    }

    /**
     * 校验验证码
     * @param code
     * @param accountNumber
     * @return
     */
    public String verificationCode(String code,String accountNumber){
        //根据邮箱从redis取值
        String redisCode = redisUtils.get(keyPrefix + ":" + accountNumber);
        if(StrUtil.isEmpty(redisCode)){
            return "验证码已过期";
        }
        //对比
        boolean equals = StrUtil.equals(code, redisCode, true);
        if(!equals){
            return "验证码错误";
        }
        return null;

    }


    /**
     * 获取用户头像路径
     */
    public String getHeadPortrait(String uid){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId,uid)
                .select(User::getHeadPortrait);
        User user = userMapper.selectOne(wrapper);
        if(BeanUtil.isEmpty(user)){
//            throw new BusinessException(ApiCode.FAIL.getCode(), "未搜索到该用户头像，请检查id");/
            return "";
        }
        return user.getHeadPortrait();
    }

    @Override
    public boolean logout(String uid) {
        //根据uid删除redis
        redisUtils.likeDel(IMConstants.IM_REDIS_ONLINE_TOKEN + uid);
        return true;
    }


}




