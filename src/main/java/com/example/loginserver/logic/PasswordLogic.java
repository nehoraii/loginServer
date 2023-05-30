package com.example.loginserver.logic;

import com.example.loginserver.enums.ErrorsEnumForPassword;
import com.example.loginserver.vo.PasswordVo;

public class PasswordLogic {
    private static ErrorsEnumForPassword checkLength(String password){
        if(password.length()<8 && password.length()>16){
            return ErrorsEnumForPassword.LengthErrorPassword;
        }
        return ErrorsEnumForPassword.GOOD;
    }
    private static ErrorsEnumForPassword checkChars(String password){
        if(!password.matches(".*[A-Z]*.")){
            return ErrorsEnumForPassword.NotBigChar;
        }
        if(!password.matches(".*[a-z]*.")){
            return ErrorsEnumForPassword.NotSmallChar;
        }
        if(!password.matches(".*[0-9]*.")){
            return ErrorsEnumForPassword.NotNumber;
        }
        return ErrorsEnumForPassword.GOOD;
    }
    public static ErrorsEnumForPassword checkPassObject(PasswordVo passwordVo){
        ErrorsEnumForPassword e;
        e=checkLength(passwordVo.getPass());
        if(e!=ErrorsEnumForPassword.GOOD){
            return e;
        }
        e=checkChars(passwordVo.getPass());
        return e;
    }
}
