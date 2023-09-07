package com.freedom.third.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "third-service")
public interface ThirdApi {

    @PostMapping("/hello")
    public String sayHello(@RequestBody String name);


    @PostMapping("/hello1")
    public String sayHello1(@RequestBody String name);
}
