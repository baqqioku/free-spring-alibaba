/*
package com.freedom.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
//@RocketMQMessageListener(topic = "guoguo${tag}", selectorType = SelectorType.SQL92, selectorExpression = "tag is not null and tag = '${tag}'", consumerGroup = "guoguotest${tag}")
public class MyConsumer implements RocketMQListener<String> {
    Logger logger = LoggerFactory.getLogger(MyConsumer.class);
    @Override
    public void onMessage(String message) {
        logger.info(message);
        RocketMQMessageListener declaredAnnotation = getClass().getDeclaredAnnotation(RocketMQMessageListener.class);
        System.out.println(declaredAnnotation.consumerGroup());

    }
}
*/
