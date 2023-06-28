package com.freedom.framework.web.swagger;

import com.free.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.net.InetAddress;
import java.util.Optional;

/**
 * @author liao
 */
@Slf4j
public class SwaggerConsole implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String appName = SpringContextUtil.getProperty("spring.application.name");
        String contextPath = Optional.ofNullable(SpringContextUtil.getProperty("server.servlet.context-path")).orElse("");
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = Optional.ofNullable(SpringContextUtil.getProperty("server.port")).orElse("8080");
        String profile = SpringContextUtil.getProfile();
        log.info("\n----------------------------------------------------------\n" +
                        "\t Application: '{}' is running! \n" +
                        "\t Environment:  {} \n" +
                        "\t Swagger Doc:  http://{}:{}{}/doc.html \n" +
                        "----------------------------------------------------------",
                appName, profile, host, port, contextPath);
    }

}
