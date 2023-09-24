package com.example.loginserver.server;

import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.logic.Security;
import com.example.loginserver.logic.UserLogic;
import com.example.loginserver.repository.UserRepository;
import com.example.loginserver.vo.UserVoPlusCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.loginserver.vo.UserVO;

import java.util.Optional;

@Service
public class UserServer {
    @Autowired
    private  UserRepository userRepository;

    public UserVoPlusCode save(UserVoPlusCode userVO){
        UserVoPlusCode userVoPlusCode=new UserVoPlusCode();
        userVO=checkUser(userVO);
        if(userVO.getE()!=ErrorsEnum.GOOD){
            userVoPlusCode.setE(userVO.getE());
            return userVoPlusCode;
        }
        String secretKey= UserLogic.getSecretKey();
        userVO.setSecretKey(secretKey);
        UserEntity bean= new UserEntity();
        BeanUtils.copyProperties(userVO,bean);
        UserEntity user;
        user=userRepository.save(bean);
        UserLogic.copyProperty(user,userVoPlusCode,userVO.getCode());
        userVoPlusCode.setE(ErrorsEnum.GOOD);
        return userVoPlusCode;

    }
    public ErrorsEnum delete(long id){
        UserEntity user=getById(id);
        if(user==null){
            return ErrorsEnum.USER_NOT_FOUND_ERROR;
        }
        userRepository.deleteById(id);
        return ErrorsEnum.GOOD;
    }
    public UserVoPlusCode update(UserVoPlusCode userVO){
        ErrorsEnum e;
        userVO=checkUserForUpdate(userVO);
        if(userVO.getE()!=ErrorsEnum.GOOD){
            return userVO;
        }
        UserEntity bean=new UserEntity();
        BeanUtils.copyProperties(userVO,bean);
        userRepository.save(bean).getId();
        userVO.setE(ErrorsEnum.GOOD);
        return userVO;
    }
    //בגלל שיש שאילתה ואני צריך את המשתנה server אז בניתי כאן את הפונקצייה בודקת אם קיים שם משתמש כזה
   private UserEntity getByUserName(String userName){
        Optional<UserEntity> user=userRepository.getByUserName(userName);
        if(!user.isPresent()){
            return null;
        }
        return user.get();
    }
    public UserVO getUserByUserName(String userName){
        userName= Security.decipherFromClientForUser(userName);
        UserEntity user=getByUserName(userName);
        UserVO userVO=new UserVO();
        if(user==null){
            userVO.setE(ErrorsEnum.USER_NOT_FOUND_ERROR);
            return userVO;
        }
        userVO.setId(user.getId());
        userVO.setE(ErrorsEnum.GOOD);
        return userVO;
    }
    //בדיקת המשתמש בכללי בעצם קורא לכל הפונקציות
    private UserVoPlusCode checkUser(UserVoPlusCode user){
        ErrorsEnum e;
        e=checkUserIsSystem(user.getUserName());
        if(e!=ErrorsEnum.GOOD){
            user.setE(e);
            return user;
        }
        e=UserLogic.checkUserName(user.getUserName());
        if(e!=ErrorsEnum.GOOD){
            user.setE(e);
            return user;
        }
        user.setCode(UserLogic.getCodeToEmail());
        e=UserLogic.checkEmail(user.getEmail(),user.getCode());
        if(e!=ErrorsEnum.GOOD){
            user.setE(e);
            return user;
        }
        e=UserLogic.checkName(user.getName());
        if(e!=ErrorsEnum.GOOD){
            user.setE(e);
            return user;
        }
        e=UserLogic.checkBirthDay(user.getBirthDay());
        user.setE(e);
        return user;
    }
    private ErrorsEnum checkUserIsSystem(String userName){
        Optional<UserEntity> user= Optional.ofNullable(getByUserName(userName));
        if(!user.isPresent()){
            return ErrorsEnum.GOOD;
        }
        return ErrorsEnum.USER_EXIST_ERROR;
    }
    private UserVoPlusCode checkUserForUpdate(UserVoPlusCode userVO){
        UserEntity user=getById(userVO.getId());
        if(user==null){
            userVO.setE(ErrorsEnum.USER_NOT_FOUND_ERROR);
            return userVO;
        }
        if(userVO.equals(user)){
            userVO.setE(ErrorsEnum.NOT_CHANGED);
            return userVO;
        }
        ErrorsEnum e;
        userVO=checkUser(userVO);
        return userVO;
    }
    private UserEntity getById(long id){
        UserEntity user;
        user=userRepository.getById(id);
        return user;
    }
    public String getSecretKey(Long userId){
        UserEntity user=userRepository.getById(userId);
        String secretCode=user.getSecretKey();
        return secretCode;
    }
}
