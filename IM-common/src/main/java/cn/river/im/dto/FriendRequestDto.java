package cn.river.im.dto;

import lombok.Data;

@Data
public class FriendRequestDto {
    //发送方id
    private String fromId;
    //接收方id
    private String toId;
}
