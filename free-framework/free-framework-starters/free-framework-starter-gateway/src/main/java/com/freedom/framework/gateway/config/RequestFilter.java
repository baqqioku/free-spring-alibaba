package com.freedom.framework.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestFilter implements GlobalFilter,GatewayFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(RequestFilter.class);

    static {
        log.info("RequestFilter init");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入 RequestFilter");
        ServerHttpRequest request = exchange.getRequest();
        request.getQueryParams().forEach((k, v) -> log.info("k:{},v:{}", k, v));
        String requestBody = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
        //exchange.getRequest();
        String method = request.getMethodValue();
        if ( HttpMethod.POST.name().equalsIgnoreCase(method)) {

            log.info("requestBody:{}",requestBody);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 运行顺序：CacheRequestBodyGatewayFilterFactory -> RequestFilterFactory
        return -10;
    }
}