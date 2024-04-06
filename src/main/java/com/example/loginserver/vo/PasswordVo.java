package com.example.loginserver.vo;

import com.example.loginserver.enums.ErrorsEnum;
import lombok.Data;

import java.util.Date;
@Data
public class PasswordVo {
    private Long id;
    private Long userId;
    private String pass;
    private ErrorsEnum e;
}
