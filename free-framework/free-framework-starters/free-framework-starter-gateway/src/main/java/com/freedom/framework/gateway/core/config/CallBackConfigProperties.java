package com.freedom.framework.gateway.core.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

//@Configuration
@Component
@ConfigurationProperties(prefix = "pay.callback")
public class CallBackConfigProperties {


    public List<String> aliPayCallBack;

    public List<String> webChatCallBack;


    public List<String> getAliPayCallBack() {
        return aliPayCallBack;
    }


    public void setAliPayCallBack(List<String> aliPayCallBack) {
        this.aliPayCallBack = aliPayCallBack;
    }


    public List<String> getWebChatCallBack() {
        return webChatCallBack;
    }

    public void setWebChatCallBack(List<String> webChatCallBack) {
        this.webChatCallBack = webChatCallBack;
    }

    @Override
    public String toString() {
        return "CallBackConfigProperties{" +
                "aliPayCallBack=" + aliPayCallBack +
                ", webChatCallBack=" + webChatCallBack +
                '}';
    }
}
