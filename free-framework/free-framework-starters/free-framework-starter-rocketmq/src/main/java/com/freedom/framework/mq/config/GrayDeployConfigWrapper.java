package com.freedom.framework.mq.config;

import com.free.common.util.TraceUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Component;

/**
 * guoguo
 * fix总线灰度的 bug ,有的配置需要在容器初始化之前就要拿到，静态方式获取配置，需要通过这种方式，要么就使用@AutoWrite的方式，
 */
@Component
public class GrayDeployConfigWrapper extends PropertySourcesPlaceholderConfigurer {


    private static PropertyResolver propertyResolver;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propertyResolver) throws BeansException {
        super.processProperties(beanFactoryToProcess, propertyResolver);
        GrayDeployConfigWrapper.propertyResolver = propertyResolver;
    }

    public static String getString(String key) {
        return propertyResolver.getProperty(key);
    }

    /**
     * 用这个可以灵活一些，可以在配置里面添加tag = green
     * @return
     */
    public static String getGroupName(){
        return propertyResolver.getProperty(TraceUtil.TAG,"");
    }


    public static String resolvePlaceholder(String key){
        return propertyResolver.resolvePlaceholders(key);
    }





}
