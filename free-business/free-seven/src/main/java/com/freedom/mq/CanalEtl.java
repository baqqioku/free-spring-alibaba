package com.freedom.mq;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "example",   consumerGroup = "g1")
public class CanalEtl implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        log.info("收到消息:{}",s);
    }
}
