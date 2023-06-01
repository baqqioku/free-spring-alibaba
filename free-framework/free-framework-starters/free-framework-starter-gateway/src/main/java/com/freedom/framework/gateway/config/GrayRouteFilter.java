package com.freedom.framework.gateway.config;

import com.free.common.util.TraceUtil;
import com.freedom.framework.gateway.core.config.GrayRouteMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.free.common.util.TraceUtil.*;
import static com.freedom.framework.gateway.core.config.RouteConstant.*;

/**
 * 优先级放在最大
 */
@Component
public class GrayRouteFilter implements GlobalFilter, Ordered {




    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String token = request.getHeaders().getFirst(TOKEN);
        String varsion = request.getHeaders().getFirst(VERSION);
        String tokenTarget = GrayRouteMap.getTokenRoute(token);
        String varsionTarget = GrayRouteMap.getVersionRoute(varsion);
        String AllTarget = GrayRouteMap.getAllRoute(ALL_VALUE);

        String finalTarget = PROD;
        if(StringUtils.isNotBlank(tokenTarget)){
            finalTarget = tokenTarget;
        }else if(StringUtils.isNotBlank(varsionTarget)){
            finalTarget = varsionTarget;
        }else if(StringUtils.isNotBlank(AllTarget)){
            finalTarget = AllTarget;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put(REQUEST_COLOR, finalTarget);
        setHeader(exchange, headers);

        return chain.filter(exchange);
    }





    @Override
    public int getOrder() {
        return -500;
    }

    /**
     * 设置请求头 批量
     *
     * @param exchange
     * @param headers
     */
    protected void setHeader(ServerWebExchange exchange, Map<String, String> headers) {
        exchange.getRequest().mutate().headers(httpHeaders -> {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                if (value != null) {
                    httpHeaders.add(key, value);
                }
            }
        });
    }
}
