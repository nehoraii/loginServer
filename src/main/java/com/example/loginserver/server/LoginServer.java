package com.example.loginserver.server;

import com.example.loginserver.entity.LoginEntity;
import com.example.loginserver.entity.PasswordEntity;
import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnumForLogin;
import com.example.loginserver.enums.ErrorsEnumForUser;
import com.example.loginserver.repository.LoginRepository;
import com.example.loginserver.repository.PasswordRepository;
import com.example.loginserver.repository.UserRepository;
import com.example.loginserver.vo.LoginVo;
import com.example.loginserver.vo.PasswordVo;
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
            loginVo.setSec(false);
            saveObject(loginVo);
            return ErrorsEnumForLogin.BLOCK;
        }
        e=checkSpam(loginVo.getUserId(),loginVo.getDate());
        if(e!=ErrorsEnumForLogin.GOOD){
            System.out.println(e);
            saveObject(loginVo);
            return ErrorsEnumForLogin.SPAM;
        }
        UserEntity user;
        user=getByUserId(loginVo.getUserId());
        if(user==null){
            saveObject(loginVo);
            loginVo.setSec(false);
            return ErrorsEnumForLogin.UserExistError;
        }
        String userPassword;
        String loginPass;
        PasswordEntity pass;
        loginPass=loginVo.getPass();
        pass=getPassByUserId(loginVo.getUserId());
        userPassword=pass.getPass();
        if(!loginPass.equals(userPassword)){
            saveObject(loginVo);
            loginVo.setSec(false);
            return ErrorsEnumForLogin.WrongPasswordError;
        }
        loginVo.setSec(true);
        e=saveObject(loginVo);
        return e;
    }
    public ErrorsEnumForLogin delete(long id){
        Optional<LoginEntity> loginEntity=getById(id);
        if (!loginEntity.isPresent()){
            return ErrorsEnumForLogin.NOT_FOUND;
        }
        loginRepository.deleteById(id);
        return ErrorsEnumForLogin.GOOD;
    }
    public ErrorsEnumForLogin update(LoginVo loginVo){
        Optional<LoginEntity> loginEntity=getById(loginVo.getId());
        if(!loginEntity.isPresent()){
            return ErrorsEnumForLogin.NOT_FOUND;
        }
        BeanUtils.copyProperties(loginVo,loginEntity.get());
        loginRepository.save(loginEntity.get());
        return ErrorsEnumForLogin.GOOD;
    }
    public LoginVo getLoginObjectByUserId(LoginVo loginVo){
        long userId=loginVo.getUserId();
        Optional<LoginEntity> loginEntity;
        try {

        loginEntity=loginRepository.getLoginObjectByUserId(userId);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
        if(!loginEntity.isPresent()){

        }
        BeanUtils.copyProperties(loginEntity.get(),loginVo);
        return loginVo;
    }
    private ErrorsEnumForLogin saveObject(LoginVo loginVo){
        LoginEntity bean= new LoginEntity();
        BeanUtils.copyProperties(loginVo,bean);
        loginRepository.save(bean);
        return ErrorsEnumForLogin.GOOD;
    }
    private Optional<LoginEntity> getById(long id){
        Optional<LoginEntity> user=loginRepository.findById(id);
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
    private UserEntity getByUserId(long userId){
        Optional<UserEntity> user;
        user=loginRepository.getUserByUserId(userId);
        if(!user.isPresent()){
            return null;
        }
        return user.get();
    }
    private PasswordEntity getPassByUserId(Long userId){
        List<PasswordEntity> password;
        password=loginRepository.getPassByUserId(userId);
        return password.get(0);
    }
}
