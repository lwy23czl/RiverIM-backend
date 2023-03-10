package cn.river.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群公告表
 * @TableName group_notice
 */
@TableName(value ="group_notice")
@Data
public class GroupNotice implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 群id
     */
    private String groupId;

    /**
     * 公告内容
     */
    private String notice;

    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}