package com.freedom.framework.mq.config;

import com.freedom.common.util.TraceUtil;
import org.apache.rocketmq.client.hook.ConsumeMessageContext;
import org.apache.rocketmq.client.hook.ConsumeMessageHook;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.List;
import java.util.Optional;

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
            String tag = messageExt.getProperty(TraceUtil.TAG);
            String traceId = messageExt.getProperty(TraceUtil.TRACE_ID);
            MDC.put(TraceUtil.TAG, Optional.ofNullable(tag).orElseGet(TraceUtil::getGrayTag));
            MDC.put(TraceUtil.TRACE_ID,Optional.ofNullable(traceId).orElseGet(TraceUtil::getTraceId));
        }

    }

    @Override
    public void consumeMessageAfter(ConsumeMessageContext consumeMessageContext) {
        MDC.clear();
    }
}
