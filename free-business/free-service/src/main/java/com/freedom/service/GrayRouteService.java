package com.freedom.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.freedom.config.GrayRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class GrayRouteService {

    @Autowired
    private ConfigService configService;


    public GrayRoute getGrayRoute() throws NacosException {
        String content = configService.getConfig("grayRoute", "DEFAULT_GROUP", 5000);
        System.out.println(content);
        //configService.addListener("grayRoute", "DEFAULT_GROUP",);
        List<GrayRoute> grayRoutes = JSON.parseArray(content, GrayRoute.class);
        if (grayRoutes != null && !grayRoutes.isEmpty()) {
            return grayRoutes.get(0);
        }
        return null;
    }
}
