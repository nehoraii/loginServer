package com.example.loginserver.controller;


import com.example.loginserver.server.PasswordServer;
import com.example.loginserver.vo.PasswordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/Password")
public class PasswordController {
    @Autowired
    private PasswordServer passwordServer;
    @PostMapping("/save")
    public long save(@RequestBody PasswordVo passwordVo){
        long userId=passwordServer.save(passwordVo);
        return userId;
    }
    @DeleteMapping("/delete")
    public long delete(PasswordVo passwordVo){
        long userId=passwordServer.delete(passwordVo.getId());
        return userId;
    }
    @PutMapping("/update")
    public long update(@RequestBody PasswordVo passwordVo){
        long userId=passwordServer.update(passwordVo);
        return userId;
    }

}
