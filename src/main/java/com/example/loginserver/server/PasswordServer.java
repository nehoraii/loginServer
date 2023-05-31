package com.example.loginserver.server;


import com.example.loginserver.entity.PasswordEntity;
import com.example.loginserver.enums.ErrorsEnumForPassword;
import com.example.loginserver.logic.PasswordLogic;
import com.example.loginserver.repository.PasswordRepository;
import com.example.loginserver.vo.PasswordVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PasswordServer {
    @Autowired
    private PasswordRepository passwordRepository;

    public PasswordServer(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public ErrorsEnumForPassword save(PasswordVo passwordVo){
        ErrorsEnumForPassword e;
        e=changePassForUpdate(passwordVo.getUserId(),passwordVo.getPass());
        if(e!=ErrorsEnumForPassword.GOOD){
            return e;
        }
        e=PasswordLogic.checkPassObject(passwordVo);
        if(e!=ErrorsEnumForPassword.GOOD){
            return e;
        }
        PasswordEntity bean= new PasswordEntity();
        BeanUtils.copyProperties(passwordVo,bean);
        passwordRepository.save(bean);
        return ErrorsEnumForPassword.GOOD;
    }

    public ErrorsEnumForPassword delete(long id){
        passwordRepository.deleteById(id);
        return ErrorsEnumForPassword.GOOD;
    }
    public ErrorsEnumForPassword update(PasswordVo passwordVo){
        ErrorsEnumForPassword e;
        e=changePassForUpdate(passwordVo.getUserId(),passwordVo.getPass());
        if(e!=ErrorsEnumForPassword.GOOD){
            return e;
        }
        PasswordEntity bean;
        bean=geyById(passwordVo.getId());
        BeanUtils.copyProperties(passwordVo,bean);
        passwordRepository.save(bean);
        return ErrorsEnumForPassword.GOOD;
    }
    private PasswordEntity geyById(long id){
        PasswordEntity user=passwordRepository.findById(id).orElseThrow(()->new NoSuchElementException("Not Found!!!"));
        return user;
    }
    private ErrorsEnumForPassword changePassForUpdate(long id,String password){
        Optional<List<PasswordEntity>> passwordEntity;
        passwordEntity=passwordRepository.getAllById(id);
        if(!passwordEntity.isPresent()){
            return ErrorsEnumForPassword.UserNotFound;
        }
        if(passwordEntity.get().get(passwordEntity.get().size()-1).getPass().equals(password)){
            return ErrorsEnumForPassword.TheSamePassword;
        }
        for (int i = 0; i < passwordEntity.get().size(); i++) {
            if(passwordEntity.get().get(i).equals(password)){
                return ErrorsEnumForPassword.PastUse;
            }
        }
        return ErrorsEnumForPassword.GOOD;
    }

}
