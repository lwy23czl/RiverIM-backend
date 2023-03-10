package cn.river.im.vo;

import lombok.Data;

@Data
public class BasicUserInfoVo {
    private String id;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String headPortrait;
    /**
     * 用户账号
     */
    private String accountNumber;
}
