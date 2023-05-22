package com.example.loginserver.server;

import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.loginserver.vo.UserVO;

@Service
public class UserServer {
    @Autowired
    private UserRepository userRepository;
    public long save(UserVO userVO){
        UserEntity bean= new UserEntity();
        BeanUtils.copyProperties(userVO,bean);
        bean=userRepository.save(bean);
        long userId=bean.getId();
        return userId;

    }
    public long delete(long id){
        userRepository.deleteById(id);
        return id;
    }
    public long update(UserVO userVO){
        UserEntity bean;
        try{
            bean=getById(userVO.getId());
        }catch (Exception e){
            e.getMessage();
            return -1;
        }
        BeanUtils.copyProperties(userVO,bean);
        long userId=userRepository.save(bean).getId();
        return userId;
    }
    private UserEntity getById(long id){
        UserEntity user=userRepository.getById(id);
        return user;
    }
}
