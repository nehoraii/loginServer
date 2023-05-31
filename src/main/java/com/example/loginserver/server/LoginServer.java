package com.example.loginserver.server;

import com.example.loginserver.entity.LoginEntity;
import com.example.loginserver.enums.ErrorsEnumForLogin;
import com.example.loginserver.enums.ErrorsEnumForUser;
import com.example.loginserver.repository.LoginRepository;
import com.example.loginserver.vo.LoginVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class LoginServer {
    private int  sizeSpam=5;
    private int sizeBlock=3;
    private int timeBlock=15;
    private int timeBetweenSpam= 15;

    public int getTimeBetweenSpam() {
        return timeBetweenSpam;
    }

    public int getSizeSpam() {
        return sizeSpam;
    }

    public int getSizeBlock() {
        return sizeBlock;
    }

    public int getTimeBlock() {
        return timeBlock;
    }

    @Autowired
    private LoginRepository loginRepository;
    public ErrorsEnumForLogin save(LoginVo loginVo){
        ErrorsEnumForLogin e;
        e=checkBlock(loginVo.getUserId(),loginVo.getDate());
        if(e!=ErrorsEnumForLogin.GOOD){
            System.out.println(e);
        }
        e=checkSpam(loginVo.getUserId(),loginVo.getDate());
        if(e!=ErrorsEnumForLogin.GOOD){
            System.out.println(e);
            return ErrorsEnumForLogin.SPAM;
        }
        LoginEntity bean= new LoginEntity();
        BeanUtils.copyProperties(loginVo,bean);
        loginRepository.save(bean);
        return ErrorsEnumForLogin.GOOD;
    }
    public long delete(long id){
        loginRepository.deleteById(id);
        return id;
    }
    public long update(LoginVo loginVo){
        LoginEntity bean;
        try{
            bean=getById(loginVo.getId());
            return bean.getId();
        }catch (Exception e){
            e.getMessage();
            return -1;
        }
    }
    private LoginEntity getById(long id){
        LoginEntity user=loginRepository.findById(id).orElseThrow(()->new NoSuchElementException("Not Found!!!"));
        return user;
    }
    private ErrorsEnumForLogin checkBlock(long id,Date dateUser){
        Optional<List<LoginEntity>> user;
        user=loginRepository.getLastThree(id,getSizeBlock());
        if(!user.isPresent()){
            return ErrorsEnumForLogin.GOOD;
        }
        if(user.get().size()<getSizeBlock()){
            return ErrorsEnumForLogin.GOOD;
        }
        for (int i = 0; i <user.get().size() ; i++) {
            if(user.get().get(i).isSec()){
                return ErrorsEnumForLogin.GOOD;
            }
        }
        int minutesUser=dateUser.getMinutes();
        int minutesLastOfUser=user.get().get(0).getDate().getMinutes();
        long minutes=minutesUser-minutesLastOfUser;
        if(minutes>getTimeBlock()){
            return ErrorsEnumForLogin.GOOD;
        }
        return ErrorsEnumForLogin.BLOCK;
    }
    private ErrorsEnumForLogin checkSpam(long id,Date dateUser){
        Optional<List<LoginEntity>>user;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateUser);
        cal.add(Calendar.MINUTE, -getTimeBetweenSpam());
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        user=loginRepository.getSpam(id,timestamp);

        if(!user.isPresent()){
            return ErrorsEnumForLogin.GOOD;
        }
        for (int i = 0; i < user.get().size(); i++) {
            if(user.get().get(i).isSec()){
                return ErrorsEnumForLogin.GOOD;
            }
        }
        if(user.get().size() > getSizeSpam()){
            return ErrorsEnumForLogin.SPAM;
        }
        return ErrorsEnumForLogin.GOOD;
    }
}
