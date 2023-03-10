package cn.river.im.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 好友请求反馈表
 * @TableName friend_requests_feedback
 */
@TableName(value ="friend_requests_feedback")
@Data
public class FriendRequestsFeedback implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 反馈接收方id
     */
    private String toId;

    /**
     * 反馈发起方id
     */
    private String fromId;

    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    /**
     * 是否同意（0同意，1不同意）
     */
    private Integer isAgree;

    /**
     * 反馈消息
     */
    private String feedbackMsg;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}