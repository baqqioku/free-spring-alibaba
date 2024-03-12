package com.freedom.framework.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * 跨域配置
 */
@Configuration
public class GatewaySpringConfig {

    private static final String ALL = "*";

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                HttpHeaders requestHeaders = request.getHeaders();
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                String[] allowedDomains = {"*.wsecar.com", "*.wsfmall.com", "*.wsfsmart.com"};
                String origin = requestHeaders.getOrigin();
                boolean match  = false;
                for (String allowedDomain : allowedDomains) {
                    match = matchesWildcard(origin, allowedDomain);
                    if(match){
                        break;
                    }
                }

                if(match){
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
                    //headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Origin,No-Cache," +
                            "X-Requested-With,If-Modified-Since,Pragma," +
                            "Last-Modified,Cache-Control,Expires,Content-Type,X-E4M-With," +
                            "userId,token,setToken,authorization,CHANNEL-SOURCE," +
                            "X-Token,X-Version,X-Uid,X-WS-Request-Color");
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                    headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "HEAD,DELETE,POST,GET,OPTIONS,PUT");
                    headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "18000L");
                }else {
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    return Mono.empty();
                }

                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }

            return chain.filter(ctx);
        };
    }


    private static String urlWildcardToRegex(String wildcard) {
        String regex = wildcard
                .replace(".", "\\.")
                .replace("*", ".*");
        return "^" + regex + "$";
    }

    private static boolean matchesWildcard(String url, String wildcard) {
        String regex = urlWildcardToRegex(wildcard);

        try {
            URL urlObj = new URL(url);
            String host = urlObj.getHost();
            return host.matches(regex);
        } catch (MalformedURLException e) {
            // Handle malformed URL
            return false;
        }
    }



}
