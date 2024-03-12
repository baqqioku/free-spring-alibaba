package com.freedom.framework.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication(scanBasePackages = "com.freedom")
@EnableDiscoveryClient
@RefreshScope
public class FreeGateWayApplication {

    public static void main(String[] args) {
        System.getProperties().put("spring.main.web-application-type","reactive");
        System.setProperty("csp.sentinel.app.type", "1");
        System.getProperties().put("tag","prod");
        SpringApplication.run(FreeGateWayApplication.class, args);
    }
}
