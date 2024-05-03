package com.example.loginserver.logic;

import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.vo.PasswordVo;

import java.util.Arrays;
//מחלקה שאחראית על כל הלוגיקה שנצטרך בשיביל הסיסמה
public class PasswordLogic {

    /*
    מקבלת: הסיסמה.
    מבצעת: בודקת אם האורך של הסיסמה עומד בסטנדרטים שהוגדרו מראש.
    מחזירה: מחזירה אם הסיסמה עומדת בתנאים ואם לא מחזירה את הסיבה.
    */
    private static ErrorsEnum checkLength(String password){
        if(password.length()<8 || password.length()>16){
            return ErrorsEnum.LENGTH_ERROR;
        }
        return ErrorsEnum.GOOD;
    }

    /*
    קבלת: הסיסמה.
    מבצעת: בודקת האם הסיסמה עומדת בסטנדרטים שהוגדרו כמו אות גדולה אחת לפחות וכן על זה הדרך.
    מחזירה: מחזירה האם הסיסמה עומדת בסטנדרטים הנדרשים ואם לא מחזירה את סיבת הבעיה.
    */
    private static ErrorsEnum checkChars(String password){
        char ch;
        boolean thereIs[]=new boolean[3];
        ErrorsEnum errorsEnum[]=new ErrorsEnum[3];
        errorsEnum[0]=ErrorsEnum.NOT_BIG_CHAR;
        errorsEnum[1]=ErrorsEnum.NOT_SMALL_CHAR;
        errorsEnum[2]=ErrorsEnum.NOT_NUMBER;
        Arrays.fill(thereIs, false);
        for (int i = 0; i < password.length(); i++) {
            ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                thereIs[0]=true;
            }
            else if (Character.isLowerCase(ch)) {
                thereIs[1]=true;
            }
            else if (Character.isDigit(ch)) {
                thereIs[2]=true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if(thereIs[i]==false){
                return errorsEnum[i];
            }
        }
        return ErrorsEnum.GOOD;
    }

    /*
    מקבלת: אובייקט PasswordVo.
    מבצעת: קוראת לכל הפונקציות שהראנו למעלה בקלאס זה.
    מחזירה: מחזירה האם הסיסמה עומדת בכל הסטנדרטים ואם לא מחזירה את סיבת הבעיה.
    */
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
