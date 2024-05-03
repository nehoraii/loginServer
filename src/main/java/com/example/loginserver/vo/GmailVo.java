package com.example.loginserver.vo;

import lombok.Data;

@Data
//מחלקה המייצגת את אובייקט האיימיל שלנו עם הפרטים הרלוונטים בלבד
public class GmailVo {
    private String sendTo;
    private String massage;
}
