package com.example.loginserver.controller;

import com.example.loginserver.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.loginserver.vo.UserVO;

@Validated
@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserServer userServer;
    @PostMapping("/save")
    public long save(@RequestBody UserVO userVO){
        long userId=userServer.save(userVO);
        return userId;
    }
    @DeleteMapping("/delete")
    public long delete(@RequestBody UserVO userVO){
        long userId=userServer.delete(userVO.getId());
        return userId;
    }
    @PutMapping("/update")
    public long update(@RequestBody UserVO userVO){
        long userId=userServer.update(userVO);
        return userId;
    }
}
