package com.freedom.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.freedom.framework.redis.lock.locker.Locker;
import com.freedom.id.generator.IdGenerator;
import com.freedom.id.generator.segment.BusinessCosIdSegmentChain;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/five")
public class FiveController {
    Logger logger = LoggerFactory.getLogger(FiveController.class);

    @Autowired
    private IdGenerator idGenerator;



    private final BusinessCosIdSegmentChain test;

    public FiveController(IdGenerator idGenerator) {
        test = idGenerator.getBusinessCosIdSegmentChain("test");
    }

    private final Object lock = new Object();

    @GetMapping("id")
    public Long testId() {
        return test.getId();
    }

    @RequestMapping("/test")
    public String test(){
        int i = new Random().nextInt(10);
        // Long id = idGenerator.getLongCosId("guoguo");
        Long id= idGenerator.getBusinessCosIdSegmentChain("test").getId();
        return id.toString();
    }


    public String test3FallBack(){
        return "error";
    }









}
