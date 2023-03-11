package cn.river.im.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.river.im.annotation.AuthCheck;
import cn.river.im.entity.User;
import cn.river.im.enums.ApiCode;
import cn.river.im.exception.BusinessException;
import cn.river.im.local.LocalUser;
import cn.river.im.result.Result;
import cn.river.im.service.UserService;
import cn.river.im.vo.BasicUserInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;
    @GetMapping("/test")
    @AuthCheck(1)
    public void test(){
        System.out.println("test......");
    }

    /**
     * 获取用户基本信息
     */
    @GetMapping("/basicUserInfo")
    @AuthCheck
    public Result<BasicUserInfoVo> getBasicUserInfo(){
        User user = LocalUser.getUser();
        BasicUserInfoVo infoVo = BeanUtil.copyProperties(user, BasicUserInfoVo.class);
        return Result.ok(infoVo);
    }

    @GetMapping("/search")
    public Result<BasicUserInfoVo> searchUser(@RequestParam("key") String key){
        if(StrUtil.isEmpty(key)){
            throw new BusinessException(ApiCode.PARAMETER_EXCEPTION.getCode(), ApiCode.PARAMETER_EXCEPTION.getMessage());
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccountNumber,key).select(User::getHeadPortrait,User::getAccountNumber,User::getNickName,User::getId);
        User one = userService.getOne(wrapper);
        if(BeanUtil.isEmpty(one)){
            throw new BusinessException(ApiCode.FAIL.getCode(), "未搜索到该用户，请检查邮箱是否正确");
        }
        BasicUserInfoVo vo = new BasicUserInfoVo();
        BeanUtil.copyProperties(one,vo);
        return Result.ok(vo);
    }

}
