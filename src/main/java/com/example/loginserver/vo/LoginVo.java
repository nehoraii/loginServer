package com.example.loginserver.vo;

import com.example.loginserver.enums.ErrorsEnum;
import lombok.Data;

import java.util.Date;

@Data
//מחלקה המייצגת את אובייקט ההתחברות עם השדות הרלוונטים לנו בלבד
public class LoginVo {
    private Long id;
    private Long userId;
    private String pass;
    private String ip;
    private boolean sec;
    private Date date;
    private String secretCode;
    private ErrorsEnum e;

}
