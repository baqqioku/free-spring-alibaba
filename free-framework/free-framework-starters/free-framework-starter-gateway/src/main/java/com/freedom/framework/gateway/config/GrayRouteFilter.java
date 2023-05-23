package com.freedom.framework.gateway.config;

import com.free.common.util.TraceUtil;
import com.freedom.gray.nacos.GrayRouteMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.free.common.util.TraceUtil.*;
import static com.freedom.gray.nacos.RouteConstant.*;

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
        headers.put(TRACE_ID, TraceUtil.getTraceId());
        setHeader(exchange, headers);

        return chain.filter(exchange);
    }





    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
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
