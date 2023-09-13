package com.freedom.controller;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.exception.NacosException;
import com.free.common.util.TraceUtil;
import com.free.common.web.vo.ResponseVo;
import com.freedom.ao.*;
import com.freedom.config.GrayRouteConfig;
import com.freedom.framework.mq.config.WsRocketMQTemplate;
import com.freedom.model.AccountTbl;
import com.freedom.model.User;
import com.freedom.model.mapper.AccountTblMapper;
import com.freedom.model.mapper.UserMapper;
import com.freedom.second.api.SecondApi;
import com.freedom.second.api.ao.FirstApi;
import com.freedom.service.FristService;
import io.swagger.annotations.Api;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/second")
@Api
public class SecondController {
    Logger logger = LoggerFactory.getLogger(SecondController.class);

 /*   @Autowired
    private WsRocketMQTemplate rocketMQTemplate;*/

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private NacosDiscoveryProperties properties;

    @Autowired
    private GrayRouteConfig grayRouteConfig;

    @Autowired
    SecondApi sercondApi;

    @Autowired
    private AccountTblMapper accountTblMapper;

    @Autowired
    private UserMapper userMapper;

    //private GrayRouteService grayRouteService;

    @Autowired
    private FristService fristService;

    @RequestMapping("/test")
    @ResponseBody
    public ResponseVo test(){
        //MDC.put(TraceUtil.TAG,"gray");
        //MDC.put(TraceUtil.TRACE_ID,TraceUtil.getTraceId());
        List<String> messages = new ArrayList<String>();
        messages.add("1");
        messages.add("2");
        messages.add("3");
        messages.add("4");
        messages.add("51");
        rocketMQTemplate.syncSend("guoguo",messages);
        return ResponseVo.success("果果你好");
    }

    @RequestMapping("/test1")
    public ResponseVo test1(){
        // final ClientServiceProvider provider = ClientServiceProvider.loadService();

          //producer.sendMessageInTransaction()
        int i = new Random().nextInt(1000);
        String key = UUID.randomUUID().toString();
        User user = new User();
        user.setId((long) i);
        user.setMoney(100L);
        /*Message message = new Message();
        message.setBody(JSON.toJSONBytes(user));
        message.setTags("trans");
        message.setTopic("trans");
        message.setKeys(key);*/

        String name = "name" + i;
        //MqMessage message = MqMessage.builder().name("事务消息" + i).msg("这是事务消息" + i).build();
        //Message<MqMessage> mqMessage = MessageBuilder.withPayload("事务消息" + i).setHeader("key", name).build();

        org.springframework.messaging.Message mqMessage = MessageBuilder.withPayload("事务消息" + i).setHeader("key", i).build();
        Map head = new HashMap<>();
        head.put("key", i);

        Message<User> message = MessageBuilder.createMessage(user,new MessageHeaders(head));
        TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction("trans",message,user);
        return ResponseVo.success(result);
    }



}
