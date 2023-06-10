package com.freedom.gray.nacos;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;

import com.free.common.util.TraceUtil;
import com.freedom.gray.config.GroupConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled
@ConditionalOnNacosDiscoveryEnabled
@ConditionalOnClass(name = {"com.alibaba.cloud.nacos.NacosDiscoveryProperties"})
public class NacosServerRegistryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public NacosDiscoveryProperties nacosProperties() {
        NacosDiscoveryProperties nacosDiscoveryProperties = new NacosDiscoveryProperties();
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        metadata.put("server.startup.time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()));

        //String group = GroupConfig.getDefaultGroupName();
        metadata.put(TraceUtil.TAG, StringUtils.isNotBlank(GroupConfig.getDefaultGroupName())?GroupConfig.getDefaultGroupName():TraceUtil.PROD);
        //这个配置默认是空值
        //System.getProperties().put(TraceUtil.TAG,StringUtils.isNotBlank(group)?StringUtils.upperCase(group):group);
        //System.getProperties().put(TraceUtil.GROUP,group);
        return nacosDiscoveryProperties;
    }


}
