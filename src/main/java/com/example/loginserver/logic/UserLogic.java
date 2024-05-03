package com.example.loginserver.logic;

import com.example.loginserver.enums.ErrorsEnum;
import org.apache.commons.codec.binary.Base32;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.Calendar;

//מחלקה שאחראית על כל הלוגיקה שנצטרך בשביל האובייקט משתמש
public class UserLogic {

    private static int sizeOfCodeToEmail=6; //שדה שמכיל את אורך הקוד לאימות כתובת המייל.
    private static  int ageConnectUser=7;//שדה המכיל את הגיל המינימאלי למשתמש.
    private static String pathToGmailServer="http://localhost:8083/sendGmail/send";
    //שדה המכיל את הכתובת לשרת המייל.
    private static int minLengthName=1; //שדה המכיל את האורך המינימאלי לשם של משתמש.

    /*
    מקבלת: "שם המשתמש" של המשתמש.
    מבצעת: בודקת אם השם משתמש עומד בכל הסטנדרטים שהוגדרו מראש.
    מחזירה: מחזירה האם השם משתמש עומד בכל הקריטריונים ואם לא מחזירה את סיבת הבעיה.
    */
    public static ErrorsEnum checkUserName(String userName){
        if(!userName.matches(".*[A-Z].*")){
            return ErrorsEnum.NOT_BIG_CAPS_ERROR;
        }
        if(!userName.matches(".*[a-z].*")){
            return ErrorsEnum.NOT_SMALL_CAPS_ERROR;
        }
        if(userName.length()<8 || userName.length()>16){
            return ErrorsEnum.LENGTH_ERROR;
        }
        return ErrorsEnum.GOOD;
    }

    /*
    מקבלת: כתובת המייל והקוד שצריך לשלוח לו.
    מבצעת: קוראת לפונקציות החיבור לשרת המייל ושולחת את הקוד לכתובת.
מחזירה: מחזירה האם ההודעה נשלחה בהצלחה ואם לא מחזירה את סיבת הבעיה.
    */
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

    /*
    מקבלת: את השם של המשתמש.
    מבצעת: בודקת את תקינות השם לפי הקריטריונים שהוגדרו.
    מחזירה: מחזירה האם השם עומד בקריטריונים אם לא מחזירה את סיבת הבעיה.
    */
    public static ErrorsEnum checkName(String name){
        if(name.contains("[0-9]")){
            return ErrorsEnum.THERE_IS_NUMBER;
        }
        if(name.length()<=minLengthName){
            return ErrorsEnum.LENGTH_ERROR;
        }
        return ErrorsEnum.GOOD;
    }

    /*
    מקבלת: את התאריך לידה של המשתמש.
    מבצעת: בודקת האם הוא עומד בדרישות הגיל של המערכת.
    מחזירה: מחזירה האם הגיל של המשתמש מתאים למערכת ואם לא מחזירה את סיבת הבעיה.
    */
    public static ErrorsEnum checkBirthDay(Date birthDay){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDay);
        LocalDate now=LocalDate.now();
        int yearsUser = calendar.get(Calendar.YEAR);
        int yearsNow=now.getYear();
        int years=yearsNow-yearsUser;
        if(years<=ageConnectUser){
            return ErrorsEnum.BIRTH_DAY_ERROR;
        }
        return ErrorsEnum.GOOD;
    }

    /*
    מקבלת: כלום.
    מבצעת: מביאה מפתח סודי העל 2 תווים רנדומאלי משתמש על ידי ספריות של Google.
    מחזירה: את המפתח הסודי.
    */
    public static String getSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);

    }
   /* public static void copyProperty(UserEntity from, UserVoPlusCode to){
        to.setId(from.getId());
        to.setName(from.getName());
        to.setEmail(from.getEmail());
        to.setSecretKey(from.getSecretKey());
        to.setPhone(from.getPhone());
        to.setSecName(from.getSecName());
        to.setUserName(from.getUserName());
        to.setBirthDay(from.getBirthDay());
    }*/
   /*    public static void copyProperty(UserEntity from, UserVoPlusCode to,String code){
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

 */


    /*
    מקבלת: כלום.
מבצעת: מביאה מספרים רנדומליים באורך שהוגדר במשתנה sizeOfCodeToEmail.
    מחזירה: מחזירה מחרוזת שבה יש את המספרים הרנדומליים.
    */
    public static String getCodeToEmail(){
        String code="";
        Random random=new Random();
        for (int i = 0; i <sizeOfCodeToEmail ; i++) {
            code+=random.nextInt(10);
        }
        System.out.println(code);
        return code;
    }
}
