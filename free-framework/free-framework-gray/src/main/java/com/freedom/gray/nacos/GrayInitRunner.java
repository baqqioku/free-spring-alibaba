package com.freedom.gray.nacos;


import com.free.common.util.TraceUtil;
import com.freedom.gray.config.GroupConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;


public class GrayInitRunner implements SpringApplicationRunListener {

    public GrayInitRunner(){

    }

    public GrayInitRunner(SpringApplication application, String[] args) {

    }

    @Override
    public void  contextPrepared(ConfigurableApplicationContext context) {
        String group = GroupConfig.getDefaultGroupName();
//        System.getProperties().put(TraceUtil.TAG, StringUtils.isNotBlank(group)?StringUtils.upperCase(group):group);
        System.getProperties().put(TraceUtil.TAG, group);
        System.getProperties().put(TraceUtil.GROUP,group);
    }
}
