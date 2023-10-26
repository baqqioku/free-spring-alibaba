package com.freedom.service;

import com.freedom.model.User;
import com.freedom.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void doSave(){
        User user =new User();
        Random random =new Random();
        int money = random.nextInt(1000);
        user.setMoney((long) money);
        userMapper.insertSelective(user);
    }
}
