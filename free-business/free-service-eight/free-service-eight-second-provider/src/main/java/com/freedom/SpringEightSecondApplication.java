package com.freedom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication(scanBasePackages = "com.freedom")
@ServletComponentScan
@EnableDiscoveryClient
//@EnableFeignClients(basePackages = "com.freedom")
@RefreshScope
public class SpringEightSecondApplication {
    public static void main(String[] args){
        SpringApplication.run(SpringEightSecondApplication.class,args);
    }
}
