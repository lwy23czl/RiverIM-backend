package cn.river.im.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDto implements Serializable {
    private String accountNumber;
    private String passWord;
}
