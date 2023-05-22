package com.example.loginserver.server;

import com.example.loginserver.entity.LoginEntity;
import com.example.loginserver.repository.LoginRepository;
import com.example.loginserver.vo.LoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class LoginServer {
    @Autowired
    private LoginRepository loginRepository;
    public long save(LoginVo loginVo){
        LoginEntity bean= new LoginEntity();
        BeanUtils.copyProperties(loginVo,bean);
        bean=loginRepository.save(bean);
        return bean.getId();
    }
    public long delete(long id){
        loginRepository.deleteById(id);
        return id;
    }
    public long update(LoginVo loginVo){
        LoginEntity bean;
        try{
            bean=geyById(loginVo.getId());
            return bean.getId();
        }catch (Exception e){
            e.getMessage();
            return -1;
        }
    }
    private LoginEntity geyById(long id){
        LoginEntity user=loginRepository.findById(id).orElseThrow(()->new NoSuchElementException("Not Found!!!"));
        return user;
    }
}
