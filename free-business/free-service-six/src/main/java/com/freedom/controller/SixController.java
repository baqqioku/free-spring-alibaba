package com.freedom.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.freedom.framework.redis.lock.locker.Locker;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/six")
@Api
public class SixController {
    Logger logger = LoggerFactory.getLogger(SixController.class);

    @Autowired
    private Locker redissonLocker;

    @RequestMapping("/test1")
    public String test1(){
        int i = new Random().nextInt(10);
        try {
            redissonLocker.tryLock("test1",-1,3, TimeUnit.SECONDS);
            Thread.sleep(1000*60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redissonLocker.unlock();
        }
        return "test1";
    }

    @RequestMapping("/test2")
    public String test2(){
        int i = new Random().nextInt(10);
        try {
            redissonLocker.lock("test2",-1, TimeUnit.SECONDS);
            Thread.sleep(1000*60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redissonLocker.unlock();
        }
        return "test2";
    }


    @SentinelResource(fallback = "test3FallBack")
    @RequestMapping("/test3")
    public String test3(){
        int i = new Random().nextInt(10);
        if(i%2==0){
            System.out.println(1/0);
        }
        return "ok";
    }


    public String test3FallBack(){
        return "error";
    }









}
