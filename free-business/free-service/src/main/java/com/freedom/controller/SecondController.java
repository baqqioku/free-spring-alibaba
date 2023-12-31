package com.freedom.controller;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.freedom.config.FirstConfig;
import com.freedom.config.GrayRouteConfig;
import com.freedom.model.AccountTbl;
import com.freedom.model.User;
import com.freedom.model.mapper.AccountTblMapper;
import com.freedom.producer.MyTransactionProducer;
import com.freedom.second.api.SecondApi;
import com.freedom.id.service.FristService;
import com.freedom.id.service.UserService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/second")
//不加这个@Value的值无法自动更新
@RefreshScope
public class SecondController {
    Logger logger = LoggerFactory.getLogger(SecondController.class);

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
    private MyTransactionProducer transactionProducer;

    @Autowired
    private FirstConfig firstConfig;

    @Value("${foo:}")
    private String foo;


    @NacosValue(value = "${foo:}",autoRefreshed = true)
    private String nacosFoo;



    /*@Autowired
    private TransactonListenerImpl transactonListener;*/


    //private GrayRouteService grayRouteService;

    @Autowired
    private FristService fristService;

    @Autowired
    private UserService userService;


    @RequestMapping("/test1")
    public String  test1() throws MQClientException, UnsupportedEncodingException {
        // final ClientServiceProvider provider = ClientServiceProvider.loadService();
       /* TransactionMQProducer producer = new TransactionMQProducer("guoguoTransaction");
        //producer.setNamesrvAddr("rocketmq01.dev02.bmpl.ws.srv:9876");
        //producer.setNamesrvAddr("172.24.224.7:9876");
        producer.setNamesrvAddr("172.16.5.88:9876");

        producer.setInstanceName(UUID.randomUUID().toString());
        producer.setTransactionListener(transactonListener);

        producer.start();*/

        for(int j=0;j<10;j++){
            String businessNo = UUID.randomUUID().toString();
            int i = new Random().nextInt(100);
            AccountTbl accountTbl = new AccountTbl();
            //accountTbl.setId(i);
            accountTbl.setMoney(i);
            accountTbl.setUserId(i+"");
            /*Message msg = new Message("TransanctionMessage", "tag", businessNo,
                    JSON.toJSONString(transferRecord).getBytes(RemotingHelper.DEFAULT_CHARSET));*/
            //SendResult sendResult = producer.sendMessageInTransaction(msg, null);
            //org.apache.rocketmq.common.message.Message message = new org.apache.rocketmq.common.message.Message("guoguo-transaction","tag", businessNo,JSON.toJSONString(accountTbl).getBytes(RemotingHelper.DEFAULT_CHARSET));
            org.apache.rocketmq.common.message.Message msg = new Message("guoguo-transaction", "tag", businessNo,
                    JSON.toJSONString(accountTbl).getBytes(RemotingHelper.DEFAULT_CHARSET));

            //SendResult sendResult = TransactionProducer.getInstance().sendMessageInTransaction(message, null);
            TransactionSendResult sendResult = transactionProducer.send("guoguo-transaction", JSON.toJSONString(accountTbl),businessNo);
            System.out.printf("%s%n", sendResult);

        }

        return "ok";
    }

    @SentinelResource
    @RequestMapping("/test2")
    public String test2(){

        for(int i=0;i<20;i++){
            //int i = new Random().nextInt(1000);
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

            org.springframework.messaging.Message<User> message = MessageBuilder.createMessage(user,new MessageHeaders(head));
            TransactionSendResult result = rocketMQTemplate.sendMessageInTransaction("trans",message,user);

        }
        return "ok";
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

    @SentinelResource(fallback = "test3FallBack")
    @RequestMapping("/test4")
    public String test4(){
        int i = new Random().nextInt(10);
        if(i%2==0){
            System.out.println(1/0);
        }

        return firstConfig.toString();
    }

    public String test3FallBack(){
        return "error";
    }


    @SentinelResource(fallback = "test3FallBack")
    @RequestMapping("/test5")
    public String test5(){

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(getFoo());
        logger.info(nacosFoo);
        return firstConfig.toString();
    }

    @RequestMapping("/test6")
    public String test6() throws InterruptedException {
        /*for(int i=0;i<10000000;i++){
            userService.doSave();
            //Thread.sleep(50);
        }*/

        ExecutorService executor = Executors.newFixedThreadPool(10);
        //for(int i=0;i<10000000;i++){
            executor.execute(() -> {
                userService.doSave();
            //});
        });

        return "ok";
    }

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    @NacosConfigListener(dataId = "free-service.properties")
    public String getNacosFoo() {
        return nacosFoo;
    }

    @NacosConfigListener(dataId = "free-service.properties")
    public void setNacosFoo(String nacosFoo) {
        this.nacosFoo = nacosFoo;
    }
}
