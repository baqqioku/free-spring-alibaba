package com.freedom.framework.mq.config;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

@Component
public class WsRocketMQTemplate {

    @Autowired
    public RocketMQTemplate rocketMQTemplate;

    @Autowired
    StandardEnvironment environment;

    public SendResult syncSend(String destination, Object payload) {
        String afterDestination = environment.resolvePlaceholders(destination);
        return rocketMQTemplate.syncSend(afterDestination, payload);
    }
}
