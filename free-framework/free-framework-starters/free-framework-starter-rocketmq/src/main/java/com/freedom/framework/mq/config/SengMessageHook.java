package com.freedom.framework.mq.config;

import com.free.common.util.TraceUtil;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class SengMessageHook implements SendMessageHook {

    private Logger logger  = LoggerFactory.getLogger(SengMessageHook.class);

    @Override
    public String hookName() {
        return "wsfSendHook";
    }

    @Override
    public void sendMessageBefore(SendMessageContext sendMessageContext) {
        String topic = sendMessageContext.getMessage().getTopic();
        GrayDeployConfigWrapper.resolvePlaceholder(topic);

        String traceId = TraceUtil.getTraceId();
        logger.info(MDC.get(TraceUtil.TAG));
        logger.info(MDC.get(topic));
        sendMessageContext.getMessage().putUserProperty(TraceUtil.TRACE_ID,traceId);
        sendMessageContext.getMessage().putUserProperty(TraceUtil.TAG, TraceUtil.getGrayTag());
    }

    @Override
    public void sendMessageAfter(SendMessageContext sendMessageContext) {
        //MDC.clear();
    }
}
