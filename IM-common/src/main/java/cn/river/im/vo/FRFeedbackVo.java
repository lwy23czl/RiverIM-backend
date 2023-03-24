package cn.river.im.vo;

import lombok.Data;

@Data
public class FRFeedbackVo {
    //对方头像
    private String  headPortrait;
    //昵称
    private String nickName;
    //备注信息
    private String feedbackMsg;
    /**
     * 是否同意（0同意，1不同意）
     */
    private Integer isAgree;
    //发送时间
    private String time;
    //对方id
    private String fromId;
}
