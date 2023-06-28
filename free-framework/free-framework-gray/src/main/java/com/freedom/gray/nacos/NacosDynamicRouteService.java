package com.freedom.gray.nacos;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnClass(value = {com.alibaba.cloud.nacos.NacosConfigManager.class})
public class NacosDynamicRouteService implements ApplicationListener<ContextRefreshedEvent> {


    private static final Logger LOGGER = LoggerFactory.getLogger(NacosDynamicRouteService.class);


    @Autowired
    private NacosConfigManager nacosConfigManager;


    /**
     * 清空路由
     */
    private void clearRoute() {
        GrayRouteMap.clean();
    }

    private void parseRoute(String routeInfo) {
        if(StringUtils.isBlank(routeInfo)){
            return;
        }
        List<GrayRoute> gatewayRouteDefinitions = JSON.parseArray(routeInfo, GrayRoute.class);
        for (GrayRoute routeDefinition : gatewayRouteDefinitions) {
            for (String routeValue : routeDefinition.getValue()) {
                GrayRouteMap.parseRoute(routeDefinition.getRouteType(), routeValue, routeDefinition.getTarget());
            }
        }
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {
            String content = nacosConfigManager.getConfigService().getConfigAndSignListener("grayRoute", "DEFAULT_GROUP", 5000, new AbstractListener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    clearRoute();
                    parseRoute(configInfo);

                }
            });
            parseRoute(content);

        } catch (NacosException e) {
            LOGGER.error("路由规则加载错误,{}", e);
            e.printStackTrace();
        }

    }
}
