package com.freedom.framework.mq.config;

import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.slf4j.MDC;

public class SengMessageHook implements SendMessageHook {
    @Override
    public String hookName() {
        return "wsfSendHook";
    }

    @Override
    public void sendMessageBefore(SendMessageContext sendMessageContext) {
        String traceId = TraceIdUtil.getTraceId();
        sendMessageContext.getMessage().putUserProperty(TraceIdUtil.TRACE_ID,traceId);
        sendMessageContext.getMessage().putUserProperty(TraceIdUtil.TAG, TraceIdUtil.getGrayTag());
    }

    @Override
    public void sendMessageAfter(SendMessageContext sendMessageContext) {
        MDC.clear();
    }
}
