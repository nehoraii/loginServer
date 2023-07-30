package com.example.loginserver.logic;

import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.vo.PasswordVo;

public class PasswordLogic {
    private static ErrorsEnum checkLength(String password){
        if(password.length()<8 && password.length()>16){
            return ErrorsEnum.LENGTH_ERROR;
        }
        return ErrorsEnum.GOOD;
    }
    private static ErrorsEnum checkChars(String password){
        if(!password.matches(".*[A-Z]*.")){
            return ErrorsEnum.NOT_BIG_CHAR;
        }
        if(!password.matches(".*[a-z]*.")){
            return ErrorsEnum.NOT_SMALL_CHAR;
        }
        if(!password.matches(".*[0-9]*.")){
            return ErrorsEnum.NOT_NUMBER;
        }
        return ErrorsEnum.GOOD;
    }
    public static ErrorsEnum checkPassObject(PasswordVo passwordVo){
        ErrorsEnum e;
        e=checkLength(passwordVo.getPass());
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        e=checkChars(passwordVo.getPass());
        return e;
    }
}
