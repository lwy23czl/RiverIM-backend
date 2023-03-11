package cn.river.im.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FeedbackDto {
    /**
     * 反馈接收方id
     */
    private String toId;



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

    /**
     * 备注
     */
    private String remarks;
}
