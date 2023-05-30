package com.freedom.second;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = "com.freedom")
@ServletComponentScan
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.freedom")
@RefreshScope
public class FreeSecondServiceApplication {



    public static void main(String[] args){
        System.getProperties().setProperty("tag","gray");
        SpringApplication.run(FreeSecondServiceApplication.class);

        //


    }
}
