package com.example.loginserver.logic;
import com.example.loginserver.enums.ErrorsEnumForUser;
import org.apache.commons.codec.binary.Base32;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;


public class UserLogic {

    private static int sizeOfCode=6;
    private static  int ageConnectUser=7;
    private static String pathToGmailServer="http://localhost:8080/sendGmail/send";
    private static int minLengthName=1;


    public static   ErrorsEnumForUser checkUserName(String userName){
        if(!userName.matches(".*[A-Z].*")){
            return ErrorsEnumForUser.NotBigSmallCapsError;
        }
        if(!userName.matches(".*[a-z].*")){
            return ErrorsEnumForUser.NotSmallCapsError;
        }
        if(userName.length()<8||userName.length()>16){
            return ErrorsEnumForUser.LengthError;
        }
        return ErrorsEnumForUser.GOOD;
    }
    public  static String getCodeToGmail(){
        Random random=new Random();
        String code="";
        String codeNow="";
        for (int i = 0; i <sizeOfCode ; i++) {
            codeNow=String.valueOf(random.nextInt(10));
            code+=codeNow;
        }
        return code;
    }

    public static ErrorsEnumForUser checkEmail(String gmail){
        ErrorsEnumForUser e;
        e=ConToServerGmail.connectToServer(pathToGmailServer,"post");
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
    public static    ErrorsEnumForUser checkName(String name){
        if(name.contains("[0-9]")){
            return ErrorsEnumForUser.ThereIsANumber;
        }
        if(name.length()<=minLengthName){
            return ErrorsEnumForUser.LengthError;
        }
        return ErrorsEnumForUser.GOOD;
    }
    public static ErrorsEnumForUser checkBirthDay(Date birthDay){
        LocalDate now=LocalDate.now();
       int yearsUser=birthDay.getYear();
       int yearsNow=now.getYear();
        int years=yearsNow-yearsUser;
        if(years<=ageConnectUser){
            return ErrorsEnumForUser.BirthDayError;
        }
        return ErrorsEnumForUser.GOOD;
    }
    public static String getSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);

    }
}
