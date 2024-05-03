package com.example.loginserver.vo;

import com.example.loginserver.enums.ErrorsEnum;
import lombok.Data;

import java.util.Date;
@Data
//המחלקה המתארת את אובייקט המשתמש ובו השדות הרלוונטים בלבד
public class UserVO {
    private Long id;
    private String name;
    private String secName;
    private String email;
    private String phone;
    private String userName;
    private Date birthDay;
    private String secretKey;
    private String code;
    private ErrorsEnum e;


}
