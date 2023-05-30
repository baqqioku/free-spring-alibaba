package com.freedom;


import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.freedom.config.FirstConfig;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FreeServiceApplication {

    @Autowired
    FirstConfig firstConfig;

    public static void main(String[] args){
        System.getProperties().setProperty("tag","gray");
        SpringApplication.run(FreeServiceApplication.class);

        //


    }
}
