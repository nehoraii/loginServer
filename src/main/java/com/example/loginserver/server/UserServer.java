package com.example.loginserver.server;

import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnumForPassword;
import com.example.loginserver.enums.ErrorsEnumForUser;
import com.example.loginserver.logic.UserLogic;
import com.example.loginserver.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.loginserver.vo.UserVO;

import java.util.List;
import java.util.Optional;

@Service
public class UserServer {
    @Autowired
    private  UserRepository userRepository;
    //private UserLogic userLogic=new UserLogic();

    public UserServer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ErrorsEnumForUser save(UserVO userVO){
        ErrorsEnumForUser e;
        e=checkUser(userVO);
        if(e!=ErrorsEnumForUser.GOOD){
            return e;
        }
        String secretKey= UserLogic.getSecretKey();
        userVO.setSecretKey(secretKey);
        UserEntity bean= new UserEntity();
        BeanUtils.copyProperties(userVO,bean);
        userRepository.save(bean);
        return ErrorsEnumForUser.GOOD;

    }
    public ErrorsEnumForUser delete(long id){
        UserEntity user=getById(id);
        if(user==null){
            return ErrorsEnumForUser.UserNotFoundError;
        }
        userRepository.deleteById(id);
        return ErrorsEnumForUser.GOOD;
    }
    public ErrorsEnumForUser update(UserVO userVO){
        ErrorsEnumForUser e;
        e=checkUserForUpdate(userVO);
        if(e!=ErrorsEnumForUser.GOOD){
            return e;
        }
        UserEntity bean=new UserEntity();
        BeanUtils.copyProperties(userVO,bean);
        userRepository.save(bean).getId();
        return ErrorsEnumForUser.GOOD;
    }
    //בגלל שיש שאילתה ואני צריך את המשתנה server אז בניתי כאן את הפונקצייה בודקת אם קיים שם משתמש כזה
   private UserEntity getByUserName(String userName){
        Optional<UserEntity> user=userRepository.getByUserName(userName);
        if(!user.isPresent()){
            return null;
        }
        return user.get();
    }
    //בדיקת המשתמש בכללי בעצם קורא לכל הפונקציות
    private ErrorsEnumForUser checkUser(UserVO user){
        ErrorsEnumForUser e;
        e=checkUserIsSystem(user.getUserName());
        if(e!=ErrorsEnumForUser.GOOD){
            return e;
        }
        e=UserLogic.checkUserName(user.getUserName());
        if(e!=ErrorsEnumForUser.GOOD){
            return e;
        }
        e=UserLogic.checkEmail(user.getEmail());
        if(e!=ErrorsEnumForUser.GOOD){
            return e;
        }
        e=UserLogic.checkName(user.getName());
        if(e!=ErrorsEnumForUser.GOOD){
            return e;
        }
        e=UserLogic.checkBirthDay(user.getBirthDay());
        return e;
    }
    private ErrorsEnumForUser checkUserIsSystem(String userName){
        Optional<UserEntity> user= Optional.ofNullable(getByUserName(userName));
        if(!user.isPresent()){
            return ErrorsEnumForUser.GOOD;
        }
        return ErrorsEnumForUser.UserExistError;
    }
    private ErrorsEnumForUser checkUserForUpdate(UserVO userVO){
        UserEntity user=getById(userVO.getId());
        if(user==null){
            return ErrorsEnumForUser.UserNotFoundError;
        }
        if(userVO.equals(user)){
            return ErrorsEnumForUser.NotChanged;
        }
        ErrorsEnumForUser e;
        e=checkUser(userVO);
        return e;
    }
    private UserEntity getById(long id){
        UserEntity user;
        user=userRepository.getById(id);
        return user;
    }
}
