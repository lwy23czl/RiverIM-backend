package cn.river.im;

import cn.river.im.dto.LoginDto;
import cn.river.im.dto.RegisterDto;
import cn.river.im.result.Result;
import cn.river.im.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class LoginTest {
    @Autowired
    private UserService userService;

    @Test
    public void registerTest(){
        RegisterDto registerDto = new RegisterDto();
        registerDto.setPassWord("123123");
        registerDto.setAccountNumber("lwy23czl@163.com");
        registerDto.setNickName("小明");
        registerDto.setCode("j8opis");
        Result<String> register = userService.register(registerDto);
        log.info(register.getMsg());
//        System.out.println(register);
    }

    @Test
    public void login(){
        LoginDto loginDto = new LoginDto();
        loginDto.setAccountNumber("lwy23czl@163.com");
        loginDto.setPassWord("123123");
        Result<String> login = userService.login(loginDto);
        log.info(login.getMsg());
        log.info(login.getData());
    }
}
