package com.example.loginserver.server;


import com.example.loginserver.entity.PasswordEntity;
import com.example.loginserver.repository.PasswordRepository;
import com.example.loginserver.vo.PasswordVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PasswordServer {
    @Autowired
    private PasswordRepository passwordRepository;
    public long save(PasswordVo passwordVo){
        PasswordEntity bean= new PasswordEntity();
        BeanUtils.copyProperties(passwordVo,bean);
        bean=passwordRepository.save(bean);
        return bean.getId();
    }
    public long delete(long id){
        passwordRepository.deleteById(id);
        return id;
    }
    public long update(PasswordVo passwordVo){
        PasswordEntity bean;
        try{
            bean=geyById(passwordVo.getId());
            return bean.getId();
        }catch (Exception e){
            e.getMessage();
            return -1;
        }
    }
    private PasswordEntity geyById(long id){
        PasswordEntity user=passwordRepository.findById(id).orElseThrow(()->new NoSuchElementException("Not Found!!!"));
        return user;
    }
}
