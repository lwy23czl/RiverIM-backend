package cn.river.im.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendMsgDto implements Serializable {
    //发送方id
    private String fromId;
    //消息类型
//    private String type; //apply 申请，chat 聊天
    //接收方id
    private String toId;
    //消息内容
    private String msg;
}