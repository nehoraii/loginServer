package com.example.loginserver.controller;

import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.server.UserServer;
import com.example.loginserver.vo.UserVoPlusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.loginserver.vo.UserVO;

@Validated
@RestController
@RequestMapping("/User")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserServer userServer;
    private ErrorsEnum e;

    @PostMapping("/save")
    public UserVoPlusCode addUser(@RequestBody UserVO userVO){
        UserVoPlusCode user;
        user=userServer.save(userVO);
        return user;
    }
   /* @PutMapping("/update")
    public UserVoPlusCode update(@RequestBody UserVoPlusCode userVO){
        UserVoPlusCode user;
        user=userServer.update(userVO);
        return user;
    }

    */
    @PostMapping("/getUser")
    public UserVO getUserUserName(@RequestBody UserVO userVO){
        UserVO user=userServer.getUserByUserName(userVO);
        return user;
    }
}
