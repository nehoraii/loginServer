package com.example.loginserver.server;

import com.example.loginserver.entity.LoginEntity;
import com.example.loginserver.entity.PasswordEntity;
import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.logic.LoginLogic;
import com.example.loginserver.logic.Security;
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
    private int  sizeSpam=50;
    private int sizeBlock=3;
    private int timeBlockMinutes=15;
    private int timeBetweenSpamMinutes= 15;
    @Autowired
    private UserServer userServer;
    @Autowired
    private LoginRepository loginRepository;

    public int getTimeBetweenSpamMinutes() {
        return timeBetweenSpamMinutes;
    }

    public int getSizeSpam() {
        return sizeSpam;
    }

    public int getSizeBlock() {
        return sizeBlock;
    }

    public int getTimeBlockMinutes() {
        return timeBlockMinutes;
    }

    public LoginVo save(LoginVo loginVo){
        String decipherPass=Security.decipherFromClientForPass(loginVo.getPass());
        loginVo.setPass(decipherPass);
        ErrorsEnum e;
        LoginVo loginVoFun=new LoginVo();
        loginVo.setDate(new Date());

        UserEntity user;
        user=getByUserId(loginVo.getUserId());
        if(user==null){
            String passToDB;
            passToDB=Security.encodeToDBPass(loginVo.getPass());
            loginVo.setPass(passToDB);
            saveObject(loginVo);
            loginVo.setSec(false);
            loginVoFun.setE(ErrorsEnum.USER_NOT_FOUND_ERROR);
            return loginVoFun;
        }
        e=checkSpam(loginVo.getUserId(),loginVo.getDate());
        if(e!=ErrorsEnum.GOOD){
            System.out.println(e);
            loginVo.setSec(false);
            //הוספתי FALSE
            saveObject(loginVo);
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
            saveObject(loginVo);
            loginVoFun.setE(ErrorsEnum.BLOCK);
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
            saveObject(loginVo);
            loginVoFun.setE(ErrorsEnum.WRONG_PASS_ERROR);
            return loginVoFun;
        }
        loginVo.setSec(true);
        String passToDB;
        passToDB=Security.encodeToDBPass(loginVo.getPass());
        loginVo.setPass(passToDB);
        saveObject(loginVo);
        loginVoFun.setE(ErrorsEnum.GOOD);
        loginVoFun.setId(loginVo.getId());
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
    /*public LoginVo getLoginObjectByUserId(LoginVo loginVo){
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
     */
    private void saveObject(LoginVo loginVo){
        LoginEntity bean= new LoginEntity();
        BeanUtils.copyProperties(loginVo,bean);
        bean=loginRepository.save(bean);
        BeanUtils.copyProperties(bean,loginVo);
    }
    private Optional<LoginEntity> getById(Long id){
        Optional<LoginEntity> user=loginRepository.findById(id);
        return user;
    }
    private ErrorsEnum checkBlock(Long id,Date dateUser){
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
        if(minutesBetween>getTimeBlockMinutes()){
            return ErrorsEnum.GOOD;
        }
        return ErrorsEnum.BLOCK;
    }
    private ErrorsEnum checkSpam(Long id,Date dateUser){
        Optional<List<LoginEntity>>user;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateUser);
        cal.add(Calendar.MINUTE, -getTimeBetweenSpamMinutes());
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        user=loginRepository.getSpam(id,timestamp);

        if(!user.isPresent()){
            return ErrorsEnum.GOOD;
        }
        if(user.get().size() < getSizeSpam()){
            return ErrorsEnum.GOOD;
        }
        for (int i = 0; i < user.get().size(); i++) {
            if(user.get().get(i).isSec()){
                return ErrorsEnum.GOOD;
            }
        }
        return ErrorsEnum.SPAM;
    }
    private UserEntity getByUserId(Long userId){
        Optional<UserEntity> user;
        user=loginRepository.getUserByUserId(userId);
        if(!user.isPresent()){
            return null;
        }
        return user.get();
    }
    private PasswordEntity getPassByUserId(Long userId){
        Object[] obj;
        obj=loginRepository.getPassByUserId(userId);
        obj=(Object[])obj[0];
        PasswordEntity password=new PasswordEntity();
        password.setId((Long) obj[0]);
        password.setUserId((Long) obj[1]);
        password.setPass((String) obj[2]);
        password.setDate((Date) obj[3]);
        return password;
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
