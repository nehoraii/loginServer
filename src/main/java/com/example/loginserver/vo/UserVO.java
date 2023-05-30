package com.example.loginserver.vo;

import lombok.Data;

import java.util.Date;
@Data
public class UserVO {
    private Long id;
    private String name;
    private String secName;
    private String email;
    private String phone;
    private String userName;
    private Date birthDay;
    private String secretKey;


}
