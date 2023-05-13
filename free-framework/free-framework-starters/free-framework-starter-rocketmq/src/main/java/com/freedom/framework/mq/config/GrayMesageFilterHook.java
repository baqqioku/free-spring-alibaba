package com.freedom.framework.mq.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.hook.FilterMessageContext;
import org.apache.rocketmq.client.hook.FilterMessageHook;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

public class GrayMesageFilterHook implements FilterMessageHook {
    private Logger logger  = LoggerFactory.getLogger(GrayMesageFilterHook.class);

    @Value("${group}")
    private String group;

    @Override
    public String hookName() {
        return "myGrayMessageFilterHook";
    }

    @Override
    public void filterMessage(FilterMessageContext context) {
        logger.info("myGrayMessageFilterHook");
        List<MessageExt> messageExtList = context.getMsgList();
        List<MessageExt> filterMessageList = messageExtList.stream().filter(messageExt -> StringUtils.equals(messageExt.getProperty("tag"), group)).collect(Collectors.toList());
        context.setMsgList(filterMessageList);



    }


}
