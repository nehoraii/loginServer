package com.example.loginserver.controller;


import com.example.loginserver.enums.ErrorsEnumForPassword;
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
    public ErrorsEnumForPassword save(@RequestBody PasswordVo passwordVo){
        ErrorsEnumForPassword e;
        e=passwordServer.save(passwordVo);
        return e;
    }
    @DeleteMapping("/delete")
    public ErrorsEnumForPassword delete(PasswordVo passwordVo){
        ErrorsEnumForPassword e;
        e=passwordServer.delete(passwordVo.getId());
        return e;
    }
    @PutMapping("/update")
    public ErrorsEnumForPassword update(@RequestBody PasswordVo passwordVo){
        ErrorsEnumForPassword e;
        e=passwordServer.update(passwordVo);
        return e;
    }

}
