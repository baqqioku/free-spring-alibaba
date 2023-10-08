package com.freedom.framework.gateway.core.config;


import com.alibaba.nacos.api.annotation.NacosProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@ConfigurationProperties(prefix = "pay.callback")
@RefreshScope
public class CallBackConfigProperties {


    public List<String> aliPayCallBack = new ArrayList<>();

    public List<String> webChatCallBack = new ArrayList<>();


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
