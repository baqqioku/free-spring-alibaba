package com.freedom.controller;

import com.freedom.framework.mq.config.TraceIdUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.free.common.web.vo.ResponseVo;
@RestController
@RequestMapping("/first")
public class FirstController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/test")
    @ResponseBody
    public ResponseVo test(){

        return ResponseVo.success("果果你好");
    }

    @RequestMapping("/test1")
    @ResponseBody
    public ResponseVo test1(){
        MDC.put(TraceIdUtil.TAG,"green");
        rocketMQTemplate.syncSend("guoguo", "hello guoguo");
        return ResponseVo.success("test1 success");
    }
}
