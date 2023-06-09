package com.example.loginserver.controller;

import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnumForUser;
import com.example.loginserver.logic.UserLogic;
import com.example.loginserver.server.UserServer;
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
public class UserController {
    @Autowired
    private UserServer userServer;
    private ErrorsEnumForUser e;
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setPort(8081);
    }

    @PostMapping("/save")
    public ErrorsEnumForUser addUser(@RequestBody UserVO userVO){
        ErrorsEnumForUser e;
        e=userServer.save(userVO);
        return e;
    }

    @DeleteMapping("/delete")
    public ErrorsEnumForUser delete(@RequestBody UserVO userVO){
        e=userServer.delete(userVO.getId());
        return ErrorsEnumForUser.GOOD;
    }
    @PutMapping("/update")
    public ErrorsEnumForUser update(@RequestBody UserVO userVO){
        e=userServer.update(userVO);
        return e;
    }
    @PostMapping("/getUser")
    public UserVO getUserUserName(@RequestBody UserVO userVO){
        UserVO user=userServer.getUserByUserName(userVO.getUserName());
        return user;
    }
}
