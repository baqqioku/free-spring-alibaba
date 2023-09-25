package com.freedom.framework.web;

import com.freedom.framework.web.config.DateTimeConfig;
import com.freedom.framework.web.config.DateTimeConvertPrimaryConfig;
import com.freedom.framework.web.swagger.SwaggerConfiguration;
import com.freedom.framework.web.swagger.SwaggerShortcutController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * @author liao
 */
@Configuration
@ComponentScan(basePackages = "com.freedom.framework.web")
@Import({SwaggerConfiguration.class, SwaggerShortcutController.class,
        DateTimeConfig.class,  DateTimeConvertPrimaryConfig.class})
public class WebAutoConfig {

    @PostConstruct
    public void addDeserializer() {
        //注册转换器
       /* SimpleModule module = new SimpleModule();
        JsonDeserializer<Enum> deserialize = new BaseEnumDeserializer();
        module.addDeserializer(Enum.class, deserialize);
        // 就是从spring容器中获取对应的bean
        ObjectMapper bean = SpringContextUtil.getBean(ObjectMapper.class);
        bean.registerModule(module);*/
    }

}
