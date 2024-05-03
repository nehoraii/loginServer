package com.example.loginserver.controller;


import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.server.PasswordServer;
import com.example.loginserver.server.UserServer;
import com.example.loginserver.vo.PasswordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/Password")
@CrossOrigin(origins = "*")
//מחלקה שאחראית לנתב את בקשות הקליינט על פי כתובת ה-URL שממנו הגיעה ובכך להפנות את בקשתו לפונקצחיה המתאימה
public class PasswordController {
    @Autowired
    private PasswordServer passwordServer; //אובייקט הכלה של הקלאס PasswordServer.
    @Autowired
    private UserServer userServer;//אובייקט הכלה של הקלאס UserServer.

    /*
    * מקבלת: אובייקט PasswordVo.
    מבצעת: שומרת את האובייקט סיסמה במסד נתונים.
    מחזירה: האם הסיסמה נשמרה בהצלחה ואם לא מחזירה את סיבת הבעיה.
    */
    @PostMapping("/save")
    public ErrorsEnum save(@RequestBody PasswordVo passwordVo){
        ErrorsEnum e;
        e=passwordServer.save(passwordVo);
        if(e!=ErrorsEnum.GOOD){
            userServer.deleteById(passwordVo.getUserId());
        }
        return e;
    }

}
