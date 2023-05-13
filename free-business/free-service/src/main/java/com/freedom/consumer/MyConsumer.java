package com.freedom.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "guoguo${tag}", selectorType = SelectorType.SQL92, selectorExpression = "tag is not null and tag = '${tag}'", consumerGroup = "guoguotest${tag}")
public class MyConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }
}
