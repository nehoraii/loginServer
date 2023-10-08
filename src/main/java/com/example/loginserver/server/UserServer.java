package com.example.loginserver.server;

import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.logic.Security;
import com.example.loginserver.logic.UserLogic;
import com.example.loginserver.repository.UserRepository;
import com.example.loginserver.vo.UserVoPlusCode;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.loginserver.vo.UserVO;

import java.util.Optional;

@Service
public class UserServer {
    @Autowired
    private  UserRepository userRepository;
    public UserVoPlusCode save(UserVO userVO){
        Security.decipherUserObjectFromClient(userVO);
        UserVoPlusCode userVoPlusCode;
        userVoPlusCode=checkUser(userVO);
        if(userVoPlusCode.getE()!=ErrorsEnum.GOOD){
            Security.encodeUserObjectToClient(userVoPlusCode);
            return userVoPlusCode;
        }

        String secretKey= UserLogic.getSecretKey();
        System.out.println(secretKey);
        userVoPlusCode.setSecretKey(secretKey);
        UserEntity bean= new UserEntity();
        BeanUtils.copyProperties(userVoPlusCode,bean);
        UserEntity user;
        Security.encodeUserObjectToDB(bean);
        user=userRepository.save(bean);
        Security.decipherUserObjectFromDB(user);
        UserLogic.copyProperty(user,userVoPlusCode);
        userVoPlusCode.setE(ErrorsEnum.GOOD);
        Security.encodeUserObjectToClient(userVoPlusCode);
        return userVoPlusCode;

    }
    /*public UserVoPlusCode update(UserVO userVO){
        ErrorsEnum e;
        UserVoPlusCode userVoPlusCode=new UserVoPlusCode();
        BeanUtils.copyProperties(userVO,userVoPlusCode);
        userVoPlusCode=checkUserForUpdate(userVO);
        if(userVoPlusCode.getE()!=ErrorsEnum.GOOD){
            return userVoPlusCode;
        }
        UserEntity bean=new UserEntity();
        BeanUtils.copyProperties(userVoPlusCode,bean);
        userRepository.save(bean).getId();
        userVO.setE(ErrorsEnum.GOOD);
        return userVO;
    }

     */
    //בגלל שיש שאילתה ואני צריך את המשתנה server אז בניתי כאן את הפונקצייה בודקת אם קיים שם משתמש כזה
   private UserEntity getByUserName(String userName){
       UserEntity userEntity=new UserEntity();
       userEntity.setUserName(userName);
       Security.encodeUserObjectToDB(userEntity);
        Optional<UserEntity> user=userRepository.getByUserName(userEntity.getUserName());
        if(!user.isPresent()){
            return null;
        }
        Security.decipherUserObjectFromDB(user.get());
        return user.get();
    }
    public UserVO getUserByUserName(UserVO userVo){
        Security.decipherUserObjectFromClient(userVo);
        UserEntity user=getByUserName(userVo.getUserName());
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
    private UserVoPlusCode checkUser(UserVO user){
        ErrorsEnum e;
        UserVoPlusCode userVoPlusCode=new UserVoPlusCode();
        BeanUtils.copyProperties(user,userVoPlusCode);
        e=checkUserIsSystem(userVoPlusCode.getUserName());
        if(e!=ErrorsEnum.GOOD){
            userVoPlusCode.setE(e);
            return userVoPlusCode;
        }
        e=UserLogic.checkUserName(userVoPlusCode.getUserName());
        if(e!=ErrorsEnum.GOOD){
            userVoPlusCode.setE(e);
            return userVoPlusCode;
        }
        userVoPlusCode.setCode(UserLogic.getCodeToEmail());
        e=UserLogic.checkEmail(userVoPlusCode.getEmail(),userVoPlusCode.getCode());
        if(e!=ErrorsEnum.GOOD){
            userVoPlusCode.setE(e);
            return userVoPlusCode;
        }
        e=UserLogic.checkName(userVoPlusCode.getName());
        if(e!=ErrorsEnum.GOOD){
            userVoPlusCode.setE(e);
            return userVoPlusCode;
        }
        e=UserLogic.checkBirthDay(userVoPlusCode.getBirthDay());
        userVoPlusCode.setE(e);
        return userVoPlusCode;
    }
    private ErrorsEnum checkUserIsSystem(String userName){
        Optional<UserEntity> user= Optional.ofNullable(getByUserName(userName));
        if(!user.isPresent()){
            return ErrorsEnum.GOOD;
        }
        return ErrorsEnum.USER_EXIST_ERROR;
    }
    private UserVoPlusCode checkUserForUpdate(UserVO userVO){
        UserEntity user=getById(userVO.getId());
        UserVoPlusCode userVoPlusCode=new UserVoPlusCode();
        BeanUtils.copyProperties(userVO,userVoPlusCode);
        if(user==null){
            userVoPlusCode.setE(ErrorsEnum.USER_NOT_FOUND_ERROR);
            return userVoPlusCode;
        }
        if(userVoPlusCode.equals(user)){
            userVoPlusCode.setE(ErrorsEnum.NOT_CHANGED);
            return userVoPlusCode;
        }
        userVoPlusCode=checkUser(userVO);
        return userVoPlusCode;
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
