package com.freedom.framework.gateway.config;

import com.freedom.framework.gateway.core.config.CallBackConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ModifyRequestBodyFilter implements GlobalFilter, Ordered {

    private Logger log = LoggerFactory.getLogger(ModifyRequestBodyFilter.class);

    @Autowired
    private CallBackConfigProperties callBackConfigProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info(callBackConfigProperties.toString());

        /**
         * save request path and serviceId into gateway context
         */
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        // 处理参数
        MediaType contentType = headers.getContentType();
        long contentLength = headers.getContentLength();
        String targetUrl = request.getURI().getPath();

        if (contentLength > 0 && request.getMethod()== HttpMethod.POST) {
            if(callBackConfigProperties.getAliPayCallBack().contains(targetUrl)|| callBackConfigProperties.webChatCallBack.contains(targetUrl)){
                return readBody(exchange, chain);
            }
        }

        return chain.filter(exchange);
    }


    /**
     * default HttpMessageReader
     */
    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();
    /**
     * ReadJsonBody
     *
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            /**
             * join the body
             */
            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    DataBufferUtils.retain(buffer);
                    return Mono.just(buffer);
                });
                /**
                 * repackage ServerHttpRequest
                 */
                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return cachedFlux;
                    }
                };
                /**
                 * mutate exchage with new ServerHttpRequest
                 */
                ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                //System.out.println(1 / 0);
                /**
                 * read body string with default messageReaders
                 */
                return ServerRequest.create(mutatedExchange, messageReaders).bodyToMono(String.class)
                        .doOnNext(objectValue -> {
                            log.info("[GatewayContext]Read JsonBody:{}", objectValue);
                        }).then(chain.filter(mutatedExchange)).doOnError(e -> {
                            e.printStackTrace();
                            chain.filter(exchange);
                        });
            }).doOnError(e -> {
                e.printStackTrace();
                chain.filter(exchange);
            });
        }catch (Exception e){
            e.printStackTrace();
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -400;
    }
}
