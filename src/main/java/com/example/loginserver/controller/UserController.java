package com.example.loginserver.controller;

import com.example.loginserver.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.loginserver.vo.UserVO;

@Validated
@RestController
@RequestMapping("/User")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserServer userServer;

    @PostMapping("/save")
    public UserVO addUser(@RequestBody UserVO userVO){
        UserVO user;
        user=userServer.save(userVO);
        return user;
    }
   /* @PutMapping("/update")
    public UserVoPlusCode update(@RequestBody UserVoPlusCode userVO){
        UserVoPlusCode user;
        user=userServer.update(userVO);
        return user;
    }

    */
    @PostMapping("/getUser")
    public UserVO getUserUserName(@RequestBody UserVO userVO){
        UserVO user=userServer.getUserByUserName(userVO);
        return user;
    }
    @PostMapping("/getUserById")
    public UserVO getUserById(@RequestBody UserVO userVO){
        UserVO user=userServer.getUserById(userVO);
        return user;
    }
}
