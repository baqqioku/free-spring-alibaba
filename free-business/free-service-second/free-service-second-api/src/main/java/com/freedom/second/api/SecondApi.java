package com.freedom.second.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name="second-service")
public interface SecondApi {

    @PostMapping("/hello")
    @ResponseBody
    public String sayHello(@RequestBody  String name);
}
