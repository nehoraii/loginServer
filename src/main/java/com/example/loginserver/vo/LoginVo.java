package com.example.loginserver.vo;

import lombok.Data;

import java.util.Date;

@Data
public class LoginVo {
    private Long id;
    private String pass;
    private String ip;
    private boolean sec;
    private String secPass;
    private Date date;

}
