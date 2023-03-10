package cn.river.im.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 聊天记录表
 * @TableName chat_record
 */
@TableName(value ="chat_record")
@Data
public class ChatRecord implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;



    /**
     * 发送方用户id
     */
    public String fromId;

    /**
     * 接收方用户id
     */
    public String toId;

    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    /**
     * 信息内容
     */
    private String content;

    /**
     * 是否是群聊类型 0否 1是
     */
    private Integer isGroupRecord;

    /**
     * 群id
     */
    private String groupId;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDel;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}