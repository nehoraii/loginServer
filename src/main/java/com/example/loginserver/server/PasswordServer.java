package com.example.loginserver.server;


import com.example.loginserver.entity.PasswordEntity;
import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.logic.PasswordLogic;
import com.example.loginserver.logic.Security;
import com.example.loginserver.repository.PasswordRepository;
import com.example.loginserver.vo.PasswordVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PasswordServer {
    @Autowired
    private PasswordRepository passwordRepository;

    public ErrorsEnum save(PasswordVo passwordVo){
        ErrorsEnum e;
        String decPass=Security.decipherFromClientForPass(passwordVo.getPass());
        passwordVo.setPass(decPass);
        e=changePassForUpdate(passwordVo.getUserId(),passwordVo.getPass());
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        e=PasswordLogic.checkPassObject(passwordVo);
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        PasswordEntity bean= new PasswordEntity();
        bean.setDate(new Date());
        BeanUtils.copyProperties(passwordVo,bean);
        String passToDB;
        passToDB=Security.encodeToDBPass(bean.getPass());
        bean.setPass(passToDB);
        passwordRepository.save(bean);
        return ErrorsEnum.GOOD;
    }
    private ErrorsEnum changePassForUpdate(long id,String password){
        Optional<List<PasswordEntity>> passwordEntity;
        passwordEntity=passwordRepository.getAllById(id);
        if(!passwordEntity.isPresent()){
            return ErrorsEnum.USER_NOT_FOUND_ERROR;
        }
        if(passwordEntity.get().size()==0){
            return ErrorsEnum.GOOD;
        }
        String passEntity=passwordEntity.get().get(passwordEntity.get().size()-1).getPass();
        if(passEntity.equals(password)){
            return ErrorsEnum.THE_SAME_PASSWORD;
        }
        for (int i = 0; i < passwordEntity.get().size(); i++) {
            if(passwordEntity.get().get(i).equals(password)){
                return ErrorsEnum.PAST_USE;
            }
        }
        return ErrorsEnum.GOOD;
    }

}
