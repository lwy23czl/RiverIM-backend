package cn.river.im.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class RegisterDto implements Serializable {
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String headPortrait;

    /**
     * 用户密码
     */
    private String passWord;

    /**
     * 用户账号
     */
    private String accountNumber;

    /**
     * 验证码
     */
    private String code;
}
