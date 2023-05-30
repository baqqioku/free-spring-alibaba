package com.freedom.gray.ribbon;


import com.netflix.loadbalancer.IRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;


/**
 * @description: RibbonAutoConfiguration
 * @Author: guoguo
 * @Date: 2023/2/3 10:54
 * @version: 1.0
 */
@Configuration
@ConditionalOnClass(name = {"com.netflix.ribbon.Ribbon","com.alibaba.cloud.nacos.ribbon.RibbonNacosAutoConfiguration","com.alibaba.cloud.nacos.discovery.configclient.NacosDiscoveryClientConfigServiceBootstrapConfiguration"})
public class RibbonAutoConfiguration {
    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public IRule balancerRule() {
        return new GrayBlancerRule();
    }
}
