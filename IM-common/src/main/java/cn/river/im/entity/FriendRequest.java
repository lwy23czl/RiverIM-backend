package cn.river.im.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 好友请求表
 * @TableName friend_request
 */
@TableName(value ="friend_request")
@Data
public class FriendRequest implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 接收方id
     */
    private String toId;

    /**
     * 请求方id
     */
    private String fromId;

    /**
     * 请求创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    /**
     * 请求验证消息
     */
    private String validationMessage;

    /**
     * 是否被处理（0否 1是）
     */
    private Integer isShow;

    /**
     * 逻辑删除
     */
    private Integer isDel;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}