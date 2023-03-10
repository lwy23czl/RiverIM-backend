package cn.river.im;

import cn.hutool.extra.mail.MailUtil;
import cn.river.im.result.Result;
import cn.river.im.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class MailTest {

    @Autowired
    private UserService userService;
    @Test
    public void sendMail(){
        MailUtil.send("lwy23czl@163.com","测试","来自river的测试邮件",false);
    }

    @Test
    public void send(){
        Result<String> verificationCode = userService.getVerificationCode("lwy23czl@163.com");
        log.info(verificationCode.getMsg());
    }
}
