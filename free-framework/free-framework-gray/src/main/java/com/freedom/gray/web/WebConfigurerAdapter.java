package com.freedom.gray.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author:
 * @Date:
 * @Description:
 */
@Configuration
@ConditionalOnMissingClass(value = "org.springframework.cloud.gateway.filter.GlobalFilter")
@ConditionalOnClass(value={WebMvcConfigurer.class})
public class WebConfigurerAdapter implements WebMvcConfigurer {
    @Bean
    @ConditionalOnMissingBean(name = "logInterceptor")
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor()).addPathPatterns("/**");
//                .excludePathPatterns("/testxx.html");
    }
}