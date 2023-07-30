package com.example.loginserver.server;

import com.example.loginserver.entity.LoginEntity;
import com.example.loginserver.entity.PasswordEntity;
import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.logic.LoginLogic;
import com.example.loginserver.repository.LoginRepository;
import com.example.loginserver.vo.LoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
    public ErrorsEnum save(LoginVo loginVo){
        ErrorsEnum e;
        loginVo.setDate(new Date());
        e=checkBlock(loginVo.getUserId(),loginVo.getDate());
        if(e!= ErrorsEnum.GOOD){
            loginVo.setSec(false);
            saveObject(loginVo);
            return ErrorsEnum.BLOCK;
        }
        e=checkSpam(loginVo.getUserId(),loginVo.getDate());
        if(e!=ErrorsEnum.GOOD){
            System.out.println(e);
            saveObject(loginVo);
            return ErrorsEnum.SPAM;
        }
        UserEntity user;
        user=getByUserId(loginVo.getUserId());
        if(user==null){
            saveObject(loginVo);
            loginVo.setSec(false);
            return ErrorsEnum.USER_NOT_FOUND_ERROR;
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
            return ErrorsEnum.WRONG_PASS_ERROR;
        }
        loginVo.setSec(true);
        e=saveObject(loginVo);
        return e;
    }
    public ErrorsEnum delete(long id){
        Optional<LoginEntity> loginEntity=getById(id);
        if (!loginEntity.isPresent()){
            return ErrorsEnum.NOT_FOUND;
        }
        loginRepository.deleteById(id);
        return ErrorsEnum.GOOD;
    }
    public ErrorsEnum update(LoginVo loginVo){
        Optional<LoginEntity> loginEntity=getById(loginVo.getId());
        if(!loginEntity.isPresent()){
            return ErrorsEnum.NOT_FOUND;
        }
        UserEntity user=getByUserId(loginVo.getUserId());
        String secretCodeRight=LoginLogic.getTOTPCode(user.getSecretKey());
        String secretCodeUser=loginVo.getSecretCode();
        if(!secretCodeUser.equals(secretCodeRight)){
            loginVo.setSec(false);
            BeanUtils.copyProperties(loginVo,loginEntity.get());
            loginRepository.save(loginEntity.get());
            return ErrorsEnum.SECRET_CODE_ERROR;
        }
        BeanUtils.copyProperties(loginVo,loginEntity.get());
        loginRepository.save(loginEntity.get());
        return ErrorsEnum.GOOD;
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
    private ErrorsEnum saveObject(LoginVo loginVo){
        LoginEntity bean= new LoginEntity();
        BeanUtils.copyProperties(loginVo,bean);
        loginRepository.save(bean);
        return ErrorsEnum.GOOD;
    }
    private Optional<LoginEntity> getById(long id){
        Optional<LoginEntity> user=loginRepository.findById(id);
        return user;
    }
    private ErrorsEnum checkBlock(long id,Date dateUser){
        Optional<List<LoginEntity>> user;
        user=loginRepository.getLastThree(id,getSizeBlock());
        if(!user.isPresent()){
            return ErrorsEnum.GOOD;
        }
        if(user.get().size()<getSizeBlock()){
            return ErrorsEnum.GOOD;
        }
        for (int i = 0; i <user.get().size() ; i++) {
            if(user.get().get(i).isSec()){
                return ErrorsEnum.GOOD;
            }
        }
        Instant instant = dateUser.toInstant();
        LocalDateTime localDateTimeUser = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        Instant instant2 = user.get().get(0).getDate().toInstant();
        LocalDateTime localDateTimeLastOfUser = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
        long minutesBetween = ChronoUnit.MINUTES.between(localDateTimeUser, localDateTimeLastOfUser);
//        int minutesUser=dateUser.getMinutes();
//        int minutesLastOfUser=user.get().get(0).getDate().getMinutes();
//        long minutes=minutesUser-minutesLastOfUser;
        if(minutesBetween>getTimeBlock()){
            return ErrorsEnum.GOOD;
        }
        return ErrorsEnum.BLOCK;
    }
    private ErrorsEnum checkSpam(long id,Date dateUser){
        Optional<List<LoginEntity>>user;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateUser);
        cal.add(Calendar.MINUTE, -getTimeBetweenSpam());
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        user=loginRepository.getSpam(id,timestamp);

        if(!user.isPresent()){
            return ErrorsEnum.GOOD;
        }
        for (int i = 0; i < user.get().size(); i++) {
            if(user.get().get(i).isSec()){
                return ErrorsEnum.GOOD;
            }
        }
        if(user.get().size() > getSizeSpam()){
            return ErrorsEnum.SPAM;
        }
        return ErrorsEnum.GOOD;
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
