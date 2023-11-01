package com.freedom.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(consumeMode = ConsumeMode.ORDERLY,topic = "orderly-TestTopic", consumerGroup = "orderComsumer")
public class OrderConsumer implements RocketMQListener {
    @Override
    public void onMessage(Object o) {
        log.info(o.toString());
    }
}
