package com.example.loginserver.logic;

import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.repository.UserRepository;
import com.example.loginserver.server.UserServer;
import com.example.loginserver.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class UserLogic {
    private  int sizeOfCode=6;
    private UserServer server= new UserServer();
    private  ErrorsEnum checkUserIsSystem(String userName){
        List<UserEntity> listUsers=server.getByUserName(userName);
        if(listUsers.isEmpty()){
            return ErrorsEnum.GOOD;
        }
        return ErrorsEnum.UserExistError;
    }
    private  ErrorsEnum checkUserName(String userName){
        if(!userName.contains("[A-Z]")){
            return ErrorsEnum.NotBigSmallCapsError;
        }
        if(!userName.contains("[a-z]")){
            return ErrorsEnum.NotSmallCapsError;
        }
        if(userName.length()<8||userName.length()>16){
            return ErrorsEnum.LengthError;
        }
        return ErrorsEnum.GOOD;
    }
    private  String getCodeToGmail(){
        Random random=new Random();
        String code="";
        String codeNow="";
        for (int i = 0; i <sizeOfCode ; i++) {
            codeNow=String.valueOf(random.nextInt(10));
            code+=codeNow;
        }
        return code;
    }

    private  ErrorsEnum checkEmail(String gmail){
        ErrorsEnum e;
        e=ConToServerGmail.connectToServer("http://localhost:8080/sendGmail/send","post");
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        String code=getCodeToGmail();
        e=ConToServerGmail.sendToServer(gmail,code);
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        e=ConToServerGmail.getFromServer();
        return e;
    }
    private  ErrorsEnum checkName(String name){
        if(name.contains("[0-9]")){
            return ErrorsEnum.ThereIsANumber;
        }
        if(name.length()<=1){
            return ErrorsEnum.LengthError;
        }
        return ErrorsEnum.GOOD;
    }
    private ErrorsEnum checkBirthDay(Date birthDay){
        Date now = new Date();
        long diffInMilliseconds=now.getTime()-birthDay.getTime();
        long diffInYears = diffInMilliseconds / (60 * 1000* 60* 24 *365);
        if(diffInYears<=7){
            return ErrorsEnum.BirthDayError;
        }
        return ErrorsEnum.GOOD;
    }
    public ErrorsEnum checkUser(UserVO user){
        ErrorsEnum e;
        e=checkUserIsSystem(user.getUserName());
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        e=checkUserName(user.getUserName());
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        e=checkEmail(user.getEmail());
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        e=checkName(user.getName());
        if(e!=ErrorsEnum.GOOD){
            return e;
        }
        e=checkBirthDay(user.getBirthDay());
        return e;
    }
    public ErrorsEnum checkUserExistById(long id){
        UserRepository repository = null;
        repository.getById(id);
        if(repository==null){
            return ErrorsEnum.UserNotFoundError;
        }
        return ErrorsEnum.GOOD;
    }
}
