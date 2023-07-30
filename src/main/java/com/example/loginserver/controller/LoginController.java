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
    public ErrorsEnum save(@RequestBody LoginVo loginVo, HttpServletRequest request){
        loginVo.setIp(request.getRemoteAddr());
        ErrorsEnum e;
        e=loginServer.save(loginVo);
        return e;
    }
    @DeleteMapping("/delete")
    public ErrorsEnum delete(@RequestBody LoginVo loginVo){
        return loginServer.delete(loginVo.getId());
    }
    @PutMapping("/update")
    public ErrorsEnum update(@RequestBody LoginVo loginVo){
        return loginServer.update(loginVo);
    }
    @PostMapping("/getLoginByUserName")
    public LoginVo getLoginObjectByUserName(@RequestBody LoginVo loginVo){
        loginVo=loginServer.getLoginObjectByUserId(loginVo);
        return loginVo;
    }
}
