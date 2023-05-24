package com.example.loginserver.controller;

import com.example.loginserver.enums.ErrorsEnum;
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
    private ErrorsEnum e;
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setPort(8081);
    }

    @PostMapping("/save")
    public ErrorsEnum addUser(@RequestBody UserVO userVO){
        UserLogic user=new UserLogic();
        e=user.checkUser(userVO);
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        userServer.save(userVO);
        return ErrorsEnum.GOOD;
    }
    @DeleteMapping("/delete")
    public ErrorsEnum delete(@RequestBody UserVO userVO){
        UserLogic user=new UserLogic();
        e=user.checkUserExistById(userVO.getId());
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        userServer.delete(userVO.getId());
        return ErrorsEnum.GOOD;
    }
    @PutMapping("/update")
    public ErrorsEnum update(@RequestBody UserVO userVO){
        UserLogic user =new UserLogic();
        e=user.checkUserExistById(userVO.getId());
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        userServer.update(userVO);
        return e;
    }
}
