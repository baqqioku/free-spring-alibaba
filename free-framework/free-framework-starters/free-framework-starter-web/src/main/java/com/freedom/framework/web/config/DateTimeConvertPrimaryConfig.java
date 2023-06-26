package com.freedom.framework.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author liao
 */
@Configuration
public class DateTimeConvertPrimaryConfig implements WebMvcConfigurer {


    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    public DateTimeConvertPrimaryConfig(MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.remove(mappingJackson2HttpMessageConverter);
        converters.add(0, mappingJackson2HttpMessageConverter);
    }

}
