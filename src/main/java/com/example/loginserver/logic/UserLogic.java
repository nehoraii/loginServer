package com.example.loginserver.logic;
import com.example.loginserver.enums.ErrorsEnumForUser;
import com.example.loginserver.repository.UserRepository;

import java.util.Date;
import java.util.Random;


public class UserLogic {

    private int sizeOfCode=6;


    public   ErrorsEnumForUser checkUserName(String userName){
        if(!userName.contains("[A-Z]")){
            return ErrorsEnumForUser.NotBigSmallCapsError;
        }
        if(!userName.contains("[a-z]")){
            return ErrorsEnumForUser.NotSmallCapsError;
        }
        if(userName.length()<8||userName.length()>16){
            return ErrorsEnumForUser.LengthError;
        }
        return ErrorsEnumForUser.GOOD;
    }
    public   String getCodeToGmail(){
        Random random=new Random();
        String code="";
        String codeNow="";
        for (int i = 0; i <sizeOfCode ; i++) {
            codeNow=String.valueOf(random.nextInt(10));
            code+=codeNow;
        }
        return code;
    }

    public ErrorsEnumForUser checkEmail(String gmail){
        ErrorsEnumForUser e;
        e=ConToServerGmail.connectToServer("http://localhost:8080/sendGmail/send","post");
        if(e!=ErrorsEnumForUser.GOOD){
            return e;
        }
        String code=getCodeToGmail();
        e=ConToServerGmail.sendToServer(gmail,code);
        if(e!=ErrorsEnumForUser.GOOD){
            return e;
        }
        e=ConToServerGmail.getFromServer();
        return e;
    }
    public    ErrorsEnumForUser checkName(String name){
        if(name.contains("[0-9]")){
            return ErrorsEnumForUser.ThereIsANumber;
        }
        if(name.length()<=1){
            return ErrorsEnumForUser.LengthError;
        }
        return ErrorsEnumForUser.GOOD;
    }
    public ErrorsEnumForUser checkBirthDay(Date birthDay){
        Date now = new Date();
        long diffInMilliseconds=now.getTime()-birthDay.getTime();
        long diffInYears = diffInMilliseconds / (60 * 1000* 60* 24 *365);
        if(diffInYears<=7){
            return ErrorsEnumForUser.BirthDayError;
        }
        return ErrorsEnumForUser.GOOD;
    }
}
