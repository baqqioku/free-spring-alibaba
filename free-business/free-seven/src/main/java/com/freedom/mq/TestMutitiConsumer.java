package com.freedom.mq;


import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "guotest",   consumerGroup = "g2",consumeMode = ConsumeMode.ORDERLY)
public class TestMutitiConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        //Message message = JSON.parseObject(s, Message.class);
        log.info("收到消息:{}",s);
    }
}
