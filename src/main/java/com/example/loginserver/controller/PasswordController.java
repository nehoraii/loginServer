package com.example.loginserver.controller;


import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.server.PasswordServer;
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
    @PostMapping("/save")
    public ErrorsEnum save(@RequestBody PasswordVo passwordVo){
        ErrorsEnum e;
        e=passwordServer.save(passwordVo);
        return e;
    }
    @DeleteMapping("/delete")
    public ErrorsEnum delete(PasswordVo passwordVo){
        ErrorsEnum e;
        e=passwordServer.delete(passwordVo.getId());
        return e;
    }
    @PutMapping("/update")
    public ErrorsEnum update(@RequestBody PasswordVo passwordVo){
        ErrorsEnum e;
        e=passwordServer.update(passwordVo);
        return e;
    }

}
