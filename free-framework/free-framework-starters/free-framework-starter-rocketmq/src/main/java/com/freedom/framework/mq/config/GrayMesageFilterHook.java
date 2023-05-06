package com.freedom.framework.mq.config;

import org.apache.rocketmq.client.hook.FilterMessageContext;
import org.apache.rocketmq.client.hook.FilterMessageHook;

public class GrayMesageFilterHook implements FilterMessageHook {
    @Override
    public String hookName() {
        return "myGrayMessageFilterHook";
    }

    @Override
    public void filterMessage(FilterMessageContext context) {

    }
}
