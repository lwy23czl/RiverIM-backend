package cn.river.im.vo;

import lombok.Data;

/**
 * 聊天对象卡片
 */
@Data
public class ChatCardVo {
    private String objectId;
    private String lastTime;
    private String lastContent;
    private String imgUrl;
    private String objectName;
}
