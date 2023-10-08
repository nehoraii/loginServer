package com.example.loginserver.controller;

import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.server.LoginServer;
import com.example.loginserver.vo.LoginVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/Login")
@CrossOrigin(origins = "*")
public class LoginController {
    @Autowired
    private LoginServer loginServer;
    @PostMapping("/save")
    public LoginVo save(@RequestBody LoginVo loginVo, HttpServletRequest request){
        loginVo.setIp(request.getRemoteAddr());
        LoginVo loginVo1;
        loginVo1=loginServer.save(loginVo);
         return loginVo1;
    }
    @PostMapping("/getLoginByUserName")
    public LoginVo getLoginObjectByUserName(@RequestBody LoginVo loginVo){
        loginVo=loginServer.getLoginObjectByUserId(loginVo);
        return loginVo;
    }
    @PostMapping("/conSecretCode")
    public LoginVo conSecretCode(@RequestBody LoginVo loginVo){
        LoginVo login;
        login=loginServer.checkSecretCode(loginVo);
        return login;
    }
}
