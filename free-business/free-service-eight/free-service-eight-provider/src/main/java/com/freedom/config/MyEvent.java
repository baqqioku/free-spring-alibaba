package com.freedom.config;

import com.freedom.mq.EightServiceComsumer;
import com.github.jaskey.rocketmq.SampleListener;
import com.github.jaskey.rocketmq.core.DedupConcurrentListener;
import com.github.jaskey.rocketmq.core.DedupConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyEvent implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("free-service-eight-consumer");
            consumer.setNamesrvAddr("127.0.0.1:9876");
            consumer.subscribe("guoguo","*");
            consumer.setMaxReconsumeTimes(1);
            consumer.setPullTimeDelayMillsWhenException(1000);
            consumer.setConsumeMessageBatchMaxSize(100);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            String appName = consumer.getConsumerGroup();
            StringRedisTemplate stringRedisTemplate= (StringRedisTemplate) event.getApplicationContext().getBean("stringRedisTemplate");
            DedupConfig dedupConfig = DedupConfig.enableDedupConsumeConfig(appName, stringRedisTemplate);
            DedupConcurrentListener messageListener = new EightServiceComsumer(dedupConfig);

            consumer.registerMessageListener(messageListener);
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        log.info("onApplicationEvent");
    }
}
