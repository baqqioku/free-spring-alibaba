package com.freedom.framework.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.freedom")
@EnableDiscoveryClient
public class FreeGateWayApplication {

    public static void main(String[] args) {
        System.getProperties().put("spring.main.web-application-type","reactive");
        SpringApplication.run(FreeGateWayApplication.class, args);
    }
}
