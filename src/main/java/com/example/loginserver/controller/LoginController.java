package com.example.loginserver.controller;

import com.example.loginserver.server.LoginServer;
import com.example.loginserver.vo.LoginVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/Login")
@CrossOrigin(origins = "*")
//מחלקה שאחראית לנתב את בקשות הקליינט על פי כתובת ה-URL שממנו הגיעה ובכך להפנות את בקשתו לפונקצחיה המתאימה
public class LoginController {
    @Autowired
    private LoginServer loginServer; //אובייקט הכלה של הקלאס loginServer

    /*
    * מקבלת: אובייקט LoginVo ואת כתובת ה-IP של המחשב מקור.
    מבצעת: שולחת את האובייקט לקלאס LoginServer וזאת לאחר ששמה את כתובת ה-IP של המחשב ששלח את הבקשה באובייקט.
    מחזירה: מחזירה אובייקט LoginVO שבתוכו יש נתונים שהקליינט צריך.
    */
    @PostMapping("/save")
    public LoginVo save(@RequestBody LoginVo loginVo, HttpServletRequest request){
        loginVo.setIp(request.getRemoteAddr());
        LoginVo loginVo1;
        loginVo1=loginServer.save(loginVo);
         return loginVo1;
    }
   /* @PostMapping("/getLoginByUserName")
    public LoginVo getLoginObjectByUserId(@RequestBody LoginVo loginVo){
        loginVo=loginServer.getLoginObjectByUserId(loginVo);
        return loginVo;
    }*/

    /*
    מקבלת: אובייקט LoginVo.
    מבצעת: בודקת אם ה-SecretCode שהמשתמש הכניס הוא נכון.
    מחזירה: מחזירה אובייקט LoginVO שבתוכו יש נתונים שהקליינט צריך.
    */
    @PostMapping("/conSecretCode")
    public LoginVo conSecretCode(@RequestBody LoginVo loginVo){
        LoginVo login;
        login=loginServer.checkSecretCode(loginVo);
        return login;
    }
}
