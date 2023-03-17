package com.freedom;


import com.freedom.config.FirstConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class FreeServiceApplication {

    @Autowired
    FirstConfig firstConfig;

    public static void main(String[] args){
        SpringApplication.run(FreeServiceApplication.class);

    }
}
