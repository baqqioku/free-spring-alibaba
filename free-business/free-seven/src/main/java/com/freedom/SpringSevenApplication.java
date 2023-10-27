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
@EnableDiscoveryClient(autoRegister = false)
@EnableAsync
//@MapperScan(basePackages = "com.freedom.**.mapper")
@Slf4j
public class SpringSevenApplication {
    public static void main(String[] args){
        System.getProperties().setProperty("tag","prod");
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringSevenApplication.class);
        configurableApplicationContext.getBeanFactory();
    }
}
