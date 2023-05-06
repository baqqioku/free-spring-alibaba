package com.freedom.framework.mq.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;

public class CoustomerRocketMQListenerContainer extends DefaultRocketMQListenerContainer {

    private DefaultMQPushConsumer grayConsumer;
}
