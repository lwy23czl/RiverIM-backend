package cn.river.im.vo;

import lombok.Data;

@Data
public class FriendRequestVo {
    //对方头像
    private String  headPortrait;
    //昵称
    private String nickName;
    //备注信息
    private String validationMessage;
    //发送时间
    private String time;
    //对方id
    private String fromId;
}
