package cn.river.im.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.river.im.annotation.AuthCheck;
import cn.river.im.entity.User;
import cn.river.im.local.LocalUser;
import cn.river.im.result.Result;
import cn.river.im.vo.BasicUserInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
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

}
