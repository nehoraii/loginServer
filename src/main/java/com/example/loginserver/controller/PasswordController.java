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
public class PasswordController {
    @Autowired
    private PasswordServer passwordServer;
    @Autowired
    private UserServer userServer;
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
