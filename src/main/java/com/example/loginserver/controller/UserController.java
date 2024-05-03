package com.example.loginserver.controller;

import com.example.loginserver.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.loginserver.vo.UserVO;

@Validated
@RestController
@RequestMapping("/User")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserServer userServer; //אובייקט הכלה של הקלאס UserServer


    /*
    מקבלת: אובייקט UserVo.
    מבצעת: שולחת את האובייקט משתמש עם כל נתוניו לקלאס ServerUser.
    מחזירה: את הנתונים שהמשתמש צריך כדי להמשיך ובנוסף מביאה לו את הקוד אימות למייל.
     */
    @PostMapping("/save")
    public UserVO addUser(@RequestBody UserVO userVO){
        UserVO user;
        user=userServer.save(userVO);
        return user;
    }

    /*
    */
    @DeleteMapping("/delete")
    public void deleteUser(@RequestBody UserVO userVO){
        userServer.deleteById(userVO.getId());
    }
   /* @PutMapping("/update")
    public UserVoPlusCode update(@RequestBody UserVoPlusCode userVO){
        UserVoPlusCode user;
        user=userServer.update(userVO);
        return user;
    }

    */


    /*
    * מקבלת: אובייקט UserVo.
    מבצעת: מבקשת מ-ServerUser את פרטי המשתמש על ידי שם המשתמש שלו .
    מחזירה: מחזירה אובייקט עם כל נתוני המשתמש הרלוונטיים.
    */
    @PostMapping("/getUser")
    public UserVO getUserUserName(@RequestBody UserVO userVO){
        UserVO user=userServer.getUserByUserName(userVO);
        return user;
    }

    /*
    מקבלת: אובייקט UserVo.
    מבצעת: מבקשת מ-ServerUser את פרטי המשתמש על ידי ה- ID שלו .
    מחזירה: מחזירה אובייקט עם כל נתוני המשתמש הרלוונטיים.
     */
    @PostMapping("/getUserById")
    public UserVO getUserById(@RequestBody UserVO userVO){
        UserVO user=userServer.getUserById(userVO);
        return user;
    }
}
