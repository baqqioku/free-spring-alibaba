package com.freedom;


import com.freedom.config.FirstConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication(scanBasePackages = "com.freedom")
@EnableDiscoveryClient
@RefreshScope
public class FreeServiceApplication {

    @Autowired
    FirstConfig firstConfig;

    public static void main(String[] args){
        SpringApplication.run(FreeServiceApplication.class);
        //System.getProperties().setProperty("spring.cloud.nacos.discovery.metadata.guoguo","guoguo");


    }
}
