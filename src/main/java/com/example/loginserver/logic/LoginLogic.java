package com.example.loginserver.logic;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import de.taimos.totp.TOTP;
//מחלקה שאחראית ללוגיקה שאנו משתמשחם בשביל האובייקט LOGIN
public class LoginLogic {
    /*
    מקבלת: המפתח הסודי של המשתמש.
    מבצעת: מחשבת את הקוד הסודי השמני באמצעות התיקיות של Google.
    מחזירה: מחזירה את הקוד הסודי הזמני.
    */
    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

}
