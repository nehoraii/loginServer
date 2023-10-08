package com.example.loginserver.logic;
import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.vo.UserVoPlusCode;
import org.apache.commons.codec.binary.Base32;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;


public class UserLogic {

    private static int sizeOfCode=6;
    private static  int ageConnectUser=7;
    private static String pathToGmailServer="http://localhost:8083/sendGmail/send";
    private static int minLengthName=1;


    public static ErrorsEnum checkUserName(String userName){
        if(!userName.matches(".*[A-Z].*")){
            return ErrorsEnum.NOT_BIG_CAPS_ERROR;
        }
        if(!userName.matches(".*[a-z].*")){
            return ErrorsEnum.NOT_SMALL_CAPS_ERROR;
        }
        if(userName.length()<8||userName.length()>16){
            return ErrorsEnum.LENGTH_ERROR;
        }
        return ErrorsEnum.GOOD;
    }

    public static ErrorsEnum checkEmail(String gmail,String code){
        ErrorsEnum e;
        e=ConToServerGmail.connectToServer(pathToGmailServer,"POST");
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        e=ConToServerGmail.sendToServer(gmail,code);
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        e=ConToServerGmail.getFromServer();
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        boolean answer;
        answer=ConToServerGmail.getAnswer();
        if(answer){
            e=ErrorsEnum.GOOD;
        }
        else{
            e=ErrorsEnum.SEND_GMAIL_ERROR;
        }
        return e;
    }
    public static    ErrorsEnum checkName(String name){
        if(name.contains("[0-9]")){
            return ErrorsEnum.THERE_IS_NUMBER;
        }
        if(name.length()<=minLengthName){
            return ErrorsEnum.LENGTH_ERROR;
        }
        return ErrorsEnum.GOOD;
    }
    public static ErrorsEnum checkBirthDay(Date birthDay){
        LocalDate now=LocalDate.now();
       int yearsUser=birthDay.getYear();
       int yearsNow=now.getYear();
        int years=yearsNow-yearsUser;
        if(years<=ageConnectUser){
            return ErrorsEnum.BIRTH_DAY_ERROR;
        }
        return ErrorsEnum.GOOD;
    }
    public static String getSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);

    }
    public static void copyProperty(UserEntity from, UserVoPlusCode to){
        to.setId(from.getId());
        to.setName(from.getName());
        to.setEmail(from.getEmail());
        to.setSecretKey(from.getSecretKey());
        to.setPhone(from.getPhone());
        to.setSecName(from.getSecName());
        to.setUserName(from.getUserName());
        to.setBirthDay(from.getBirthDay());
    }
    public static void copyProperty(UserEntity from, UserVoPlusCode to,String code){
        to.setId(from.getId());
        to.setName(from.getName());
        to.setEmail(from.getEmail());
        to.setSecretKey(from.getSecretKey());
        to.setPhone(from.getPhone());
        to.setSecName(from.getSecName());
        to.setUserName(from.getUserName());
        to.setBirthDay(from.getBirthDay());
        to.setCode(code);
    }
    public static String getCodeToEmail(){
        String code="";
        Random random=new Random();
        for (int i = 0; i <sizeOfCode ; i++) {
            code+=random.nextInt(10);
        }
        return code;
    }
}
