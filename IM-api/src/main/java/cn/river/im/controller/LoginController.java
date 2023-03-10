package cn.river.im.controller;

import cn.river.im.dto.LoginDto;
import cn.river.im.dto.RegisterDto;
import cn.river.im.enums.ApiCode;
import cn.river.im.result.Result;
import cn.river.im.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginController {

    private final UserService userService;

    /**
     * 登录
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

    /**
     * 注册
     * @param registerDto
     * @return
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterDto registerDto){
        return userService.register(registerDto);

    }

    /**
     * 获取验证码
     * @param accountNumber
     * @return
     */
    @GetMapping("/code")
    public Result<String> getVerificationCode(@RequestParam("accountNumber") String accountNumber){
        return userService.getVerificationCode(accountNumber);
    }
}
