package com.freedom.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@NacosConfigurationProperties(dataId = "grayRoute", groupId = "DEFAULT_GROUP", autoRefreshed = true,type = ConfigType.JSON )
public class GrayRouteConfig {
    private List<GrayRoute> routes;

    public List<GrayRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(List<GrayRoute> routes) {
        this.routes = routes;
    }
}
