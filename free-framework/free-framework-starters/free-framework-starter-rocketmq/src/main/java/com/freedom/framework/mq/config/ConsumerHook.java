package com.freedom.framework.mq.config;

import org.apache.rocketmq.client.hook.ConsumeMessageContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.List;

public class ConsumerHook implements ConsumeMessageHook {
    private Logger logger  = LoggerFactory.getLogger(ConsumerHook.class);

    @Override
    public String hookName() {
        return "wsfConsumer";
    }

    @Override
    public void consumeMessageBefore(ConsumeMessageContext consumeMessageContext) {
        List<MessageExt> msgList = consumeMessageContext.getMsgList();
        for(MessageExt messageExt : msgList){
            String tag = messageExt.getProperty(TraceIdUtil.TAG);
            logger.info(tag);
            MDC.put(TraceIdUtil.TAG,tag);
        }
    }

    @Override
    public void consumeMessageAfter(ConsumeMessageContext consumeMessageContext) {
        MDC.clear();
    }
}
