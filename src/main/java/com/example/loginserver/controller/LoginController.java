package com.example.loginserver.controller;

import com.example.loginserver.server.LoginServer;
import com.example.loginserver.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/Login")
public class LoginController {
    @Autowired
    private LoginServer loginServer;
    @PostMapping("/save")
    public long save(@RequestBody LoginVo loginVo){
        return loginServer.save(loginVo);
    }
    @DeleteMapping("/delete")
    public long delete(@RequestBody LoginVo loginVo){
        return loginServer.delete(loginVo.getId());
    }
    @PutMapping("/update")
    public long update(@RequestBody LoginVo loginVo){
        long userId=loginServer.update(loginVo);
        return userId;
    }
}