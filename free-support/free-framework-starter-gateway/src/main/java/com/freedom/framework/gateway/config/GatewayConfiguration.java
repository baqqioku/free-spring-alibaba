package com.freedom.framework.gateway.config;


import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.fastjson.JSON;
import com.freedom.common.web.vo.ResponseVo;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

/**
 * @author liao
 */
@Configuration
//不加这个自定义的异常还不生效
@Order(HIGHEST_PRECEDENCE)
public class GatewayConfiguration {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;


    public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @org.springframework.core.annotation.Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // Register the block exception handler for Spring Cloud Gateway.
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    @Bean
    //@ConditionalOnMissingBean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void initBlockHandlers() {
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
            /*    Map map = new HashMap<>();
                map.put("code", 0);
                map.put("message", "接口被限流了");*/

                ResponseVo<Object> fail = ResponseVo.error("熔断降级了");
                return ServerResponse.status(TOO_MANY_REQUESTS).
                        contentType(MediaType.APPLICATION_JSON_UTF8).
                        body(BodyInserters.fromObject(fail));
            }
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

    private void initCustomizedApis() {
        /*
        ApiDefinition：用户自定义的 API 定义分组，可以看做是一些 URL 匹配的组合。
        比如我们可以定义一个 API 叫 my_api，请求 path 模式为 /foo/** 和 /baz/** 的都归到 my_api 这个 API 分组下面。
        限流的时候可以针对这个自定义的 API 分组维度进行限流。
        */
        Set<ApiDefinition> definitions = new HashSet<>();
        ApiDefinition api1 = new ApiDefinition("combat_gateway_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    add(new ApiPathPredicateItem().setPattern("/nacos-provider/loadBanlance/print"));
                }});

        definitions.add(api1);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    private void initGatewayRules() {
        /*
        GatewayFlowRule：网关限流规则，
        针对 API Gateway 的场景定制的限流规则，可以针对不同 route 或自定义的 API 分组进行限流，
        支持针对请求中的参数、Header、来源 IP 等进行定制化的限流。
        */
            Set<GatewayFlowRule> rules = new HashSet<>();

        /*设置限流规则
        count: QPS即每秒钟允许的调用次数
        intervalSec: 每隔多少时间统计一次汇总数据，统计时间窗口，单位是秒，默认是 1 秒。
        */
            rules.add(new GatewayFlowRule("combat_gateway_api")
                    .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                    .setCount(5)
                    .setIntervalSec(1)

            );
            GatewayRuleManager.loadRules(rules);
        }



}
