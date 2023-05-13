package com.freedom.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

//@Configuration
public class NacosConfig {

    @Autowired
    private Environment env;

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Bean
    public ConfigService configService() throws NacosException {
        return new NacosConfigManager(nacosConfigProperties).getConfigService();
    }


}
