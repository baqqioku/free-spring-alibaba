package com.freedom.framework.mq.config;

import org.apache.rocketmq.client.hook.FilterMessageHook;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class RocketMQListenerContainerAfterProcess implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(bean instanceof DefaultRocketMQListenerContainer){
            DefaultRocketMQListenerContainer defaultRocketMQListenerContainer = (DefaultRocketMQListenerContainer) bean;
            if( defaultRocketMQListenerContainer.getConsumer()!=null){
                defaultRocketMQListenerContainer.getConsumer().getDefaultMQPushConsumerImpl().registerConsumeMessageHook(new ConsumerHook());
            }

            return defaultRocketMQListenerContainer;
        }

        if(bean instanceof RocketMQTemplate){
            RocketMQTemplate template = (RocketMQTemplate) bean;
            if(template.getProducer()!=null){
                template.getProducer().getDefaultMQProducerImpl().registerSendMessageHook(new SengMessageHook());
            }
            return template;
        }

        return bean;
    }
}
