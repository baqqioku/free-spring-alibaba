package com.freedom.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

//@Component
@RocketMQMessageListener(topic = "guoguo", selectorType = SelectorType.SQL92, selectorExpression = "tag is not null and tag = prod", consumerGroup = "guoguotest")
public class MyConsumer1 implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }
}
