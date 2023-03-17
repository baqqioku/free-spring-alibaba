package com.freedom.service;

import com.freedom.config.FirstConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class FristService {

    @Autowired
    private FirstConfig firstConfig;

    @PostConstruct
    public void test(){
        System.out.println(firstConfig.toString());
    }
}
