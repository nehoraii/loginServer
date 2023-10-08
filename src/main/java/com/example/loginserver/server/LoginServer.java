package com.example.loginserver.server;

import com.example.loginserver.entity.LoginEntity;
import com.example.loginserver.entity.PasswordEntity;
import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.logic.LoginLogic;
import com.example.loginserver.logic.Security;
import com.example.loginserver.repository.LoginRepository;
import com.example.loginserver.vo.LoginVo;
import com.example.loginserver.vo.UserVoPlusCode;
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
    private int  sizeSpam=50;
    private int sizeBlock=45;//change to 3
    private int timeBlock=15;
    private int timeBetweenSpam= 15;
    @Autowired
    private UserServer userServer;

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
    public LoginVo save(LoginVo loginVo){
        String decipherPass=Security.decipherFromClientForPass(loginVo.getPass());
        loginVo.setPass(decipherPass);
        ErrorsEnum e;
        LoginVo loginVoFun;
        loginVo.setDate(new Date());
        e=checkSpam(loginVo.getUserId(),loginVo.getDate());
        if(e!=ErrorsEnum.GOOD){
            System.out.println(e);
            loginVoFun=saveObject(loginVo);
            String passToDB;
            passToDB=Security.encodeToDBPass(loginVo.getPass());
            loginVo.setPass(passToDB);
            loginVoFun.setE(ErrorsEnum.SPAM);
            return loginVoFun;
        }
        e=checkBlock(loginVo.getUserId(),loginVo.getDate());
        if(e!= ErrorsEnum.GOOD){
            loginVo.setSec(false);
            String passToDB;
            passToDB=Security.encodeToDBPass(loginVo.getPass());
            loginVo.setPass(passToDB);
            loginVoFun=saveObject(loginVo);
            loginVoFun.setE(ErrorsEnum.BLOCK);
            return loginVoFun;
        }
        UserEntity user;
        user=getByUserId(loginVo.getUserId());
        if(user==null){
            String passToDB;
            passToDB=Security.encodeToDBPass(loginVo.getPass());
            loginVo.setPass(passToDB);
            loginVoFun=saveObject(loginVo);
            loginVo.setSec(false);
            loginVoFun.setE(ErrorsEnum.USER_NOT_FOUND_ERROR);
            return loginVoFun;
        }
        String loginPass;
        PasswordEntity pass;
        loginPass= loginVo.getPass();
        pass=getPassByUserId(loginVo.getUserId());
        String userPass;
        userPass=Security.decipherFromDBPass(pass.getPass());
        if(!loginPass.equals(userPass)){
            String passToDB;
            passToDB=Security.encodeToDBPass(loginVo.getPass());
            loginVo.setPass(passToDB);
            loginVo.setSec(false);
            loginVoFun=saveObject(loginVo);
            loginVoFun.setE(ErrorsEnum.WRONG_PASS_ERROR);
            return loginVoFun;
        }
        loginVo.setSec(true);
        String passToDB;
        passToDB=Security.encodeToDBPass(loginVo.getPass());
        loginVo.setPass(passToDB);
        loginVoFun=saveObject(loginVo);
        loginVoFun.setE(ErrorsEnum.GOOD);
        return loginVoFun;
    }
    private ErrorsEnum updateToSecretKey(LoginVo loginVo){
        Optional<LoginEntity> loginEntity=getById(loginVo.getId());
        if(!loginEntity.isPresent()){
            return ErrorsEnum.NOT_FOUND;
        }
        loginEntity.get().setSecretCode(loginVo.getSecretCode());
        loginEntity.get().setSec(loginVo.isSec());
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
            return null;
        }
        loginEntity.get().setPass(loginEntity.get().getPass());
        BeanUtils.copyProperties(loginEntity.get(),loginVo);
        return loginVo;
    }
    private LoginVo saveObject(LoginVo loginVo){
        LoginEntity bean= new LoginEntity();
        BeanUtils.copyProperties(loginVo,bean);
        LoginEntity loginEntity=loginRepository.save(bean);
        LoginVo loginVo1=new LoginVo();
        LoginLogic.copyProperty(loginEntity,loginVo1);
        return loginVo1;
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
        if(user.get().size() > getSizeSpam()){
            return ErrorsEnum.SPAM;
        }
        for (int i = 0; i < user.get().size(); i++) {
            if(user.get().get(i).isSec()){
                return ErrorsEnum.GOOD;
            }
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
    public LoginVo checkSecretCode(LoginVo loginVo){
        String key=userServer.getSecretKey(loginVo.getUserId());
        UserEntity userEntity=new UserEntity();
        userEntity.setSecretKey(key);
        Security.decipherUserObjectFromDB(userEntity);
        key=userEntity.getSecretKey();
        String code=LoginLogic.getTOTPCode(key);
        if(code.equals(loginVo.getSecretCode())){
            loginVo.setE(ErrorsEnum.GOOD);
            loginVo.setSec(true);
            updateToSecretKey(loginVo);
            return loginVo;
        }
        else {
            loginVo.setSec(false);
            loginVo.setE(ErrorsEnum.SECRET_CODE_ERROR);
            updateToSecretKey(loginVo);
        }
        return loginVo;
    }
}
