package com.example.loginserver.vo;

import com.example.loginserver.enums.ErrorsEnum;
import lombok.Data;

import java.util.Date;

@Data
public class LoginVo {
    private Long id;
    private long userId;
    private String pass;
    private String ip;
    private boolean sec;
    private Date date;
    private String secretCode;
    private ErrorsEnum e;

}
