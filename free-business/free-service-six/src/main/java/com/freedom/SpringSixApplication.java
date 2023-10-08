package com.freedom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.freedom")
@ServletComponentScan
@EnableDiscoveryClient

@EnableAsync
//@MapperScan(basePackages = "com.freedom.**.mapper")
@Slf4j
public class SpringSixApplication {
    public static void main(String[] args){
        System.getProperties().setProperty("tag","prod");
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringSixApplication.class);
        configurableApplicationContext.getBeanFactory();
    }
}
