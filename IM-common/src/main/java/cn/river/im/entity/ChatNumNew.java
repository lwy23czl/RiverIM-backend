package cn.river.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 未查看聊天信息数量表
 * @TableName chat_num_new
 */
@TableName(value ="chat_num_new")
@Data
public class ChatNumNew implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 接收方id
     */
    private Long toId;

    /**
     * 发送方id
     */
    private Long fromId;

    /**
     * 未查看聊天记录数量
     */
    private Integer num;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}