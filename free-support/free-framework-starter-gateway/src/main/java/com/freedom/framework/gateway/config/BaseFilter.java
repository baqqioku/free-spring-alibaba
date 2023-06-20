package com.freedom.framework.gateway.config;

import com.freedom.framework.gateway.core.config.HeaderKey;
import com.freedom.framework.gateway.utils.IpAddressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component
public class BaseFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(BaseFilter.class);

    private final static String H5 = "/api/mobile";
    private final static String APPLETS = "/api/applets";
    private static final String TRACE_ID = "traceId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //对请求对象request进行增强
        ServerHttpRequest req = request.mutate().headers(httpHeaders -> {
            //httpHeaders 封装了所有的请求头
            String traceId = UUID.randomUUID().toString().replace("-", "");
            httpHeaders.set(TRACE_ID, traceId);
        }).build();

        String source = "-1";
        String path = exchange.getRequest().getPath().toString();
        String channelSource = exchange.getRequest().getHeaders().getFirst(HeaderKey.CHANNEL_SOURCE);
        String ip = IpAddressUtils.getIp(exchange.getRequest());
        if(path.contains(H5)){
            source = "2";
        } else if (path.contains(APPLETS)){
            source = "3";
        }
        log.info("path： {}", path);
        log.info("channelSource： {}", channelSource);
        log.info("ip： {}", ip);
        //设置请求头
        Map<String, String> headers = new HashMap<>();
        headers.put(HeaderKey.SOURCE, source);
        headers.put(HeaderKey.CHANNEL_SOURCE, channelSource);
        headers.put(HeaderKey.X_FORWARDED_FOR, ip);
        setHeader(exchange, headers);
        return chain.filter(exchange);
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

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
