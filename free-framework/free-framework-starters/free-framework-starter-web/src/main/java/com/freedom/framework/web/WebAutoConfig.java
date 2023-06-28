package com.freedom.framework.web;

import com.freedom.framework.web.config.DateTimeConfig;
import com.freedom.framework.web.config.DateTimeConvertPrimaryConfig;
import com.freedom.framework.web.swagger.SwaggerConfiguration;
import com.freedom.framework.web.swagger.SwaggerShortcutController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liao
 */
@Configuration
@ComponentScan(basePackages = "com.freedom.framework.web")
@Import({SwaggerConfiguration.class, SwaggerShortcutController.class,
        DateTimeConfig.class,  DateTimeConvertPrimaryConfig.class})
public class WebAutoConfig {

}
