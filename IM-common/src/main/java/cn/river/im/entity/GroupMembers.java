package cn.river.im.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群成员表
 * @TableName group_members
 */
@TableName(value ="group_members")
@Data
public class GroupMembers implements Serializable {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 群id
     */
    private String groupId;

    /**
     * 用户备注
     */
    private String remarks;

    /**
     * 用户进群时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDel;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}