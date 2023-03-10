package cn.river.im.service;

import cn.river.im.dto.LoginDto;
import cn.river.im.dto.RegisterDto;
import cn.river.im.entity.User;
import cn.river.im.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 阳名
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-11-03 15:28:23
*/
public interface UserService extends IService<User> {
    Result<String> login(LoginDto loginDto);

    Result<String> register(RegisterDto registerDto);

    Result<String> getVerificationCode(String accountNumber);

    String getHeadPortrait(String uid);
}
