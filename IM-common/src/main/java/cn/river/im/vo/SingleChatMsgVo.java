package cn.river.im.vo;

import lombok.Data;

@Data
public class SingleChatMsgVo {
    //接收方id
    private String toId;
    //发送方id
    private String fromId;
    //发送时间
    private String sendingTime;
    //消息内容
    private String msg;
    //发送方头像
    private String headPortrait;
    //发送方昵称
    private String nickName;
}
