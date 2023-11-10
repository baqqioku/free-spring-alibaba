package com.freedom.eight.second.service;

import com.freedom.eight.api.SecondService;
import com.freedom.model.TAccount;
import com.freedom.model.mapper.TAccountMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

@DubboService(cluster = "failfast",retries = 0)
public class SecondServiceImpl implements SecondService {

    @Autowired
    TAccountMapper tAccountMapper;

    @Override
    public String secondSyaHello(String name) {
        TAccount account = new TAccount();
        account.setUserId(name);
        account.setUserId("23");
        tAccountMapper.insertSelective(account);
        Random random = new Random();
        if(random.nextInt(10) % 2==0){
            System.out.println(1/0);
        };
        return name;
    }
}
