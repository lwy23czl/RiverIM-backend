package cn.river.im.vo;

import lombok.Data;

/**
 * 页面显示的消息对象
 */
@Data
public class ChatMsgItemVo {
    //是否自己发送的消息
    private boolean me;
    //头像
    private String avatar;
    //消息内容
    private String content;
    //消息时间
    private String time;
    //昵称
    private String nickName;
}
