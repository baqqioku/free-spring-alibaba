package com.freedom.second.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freedom.second.api.ao.FirstApi;
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


    @PostMapping("/hello1")
    @ResponseBody
    @JsonDeserialize(using = CustomEnumSerializer.class) // 使用自定义的序列化器
    public FirstApi sayHello1(@RequestBody  String name);

    @PostMapping("/hello2")
    @ResponseBody
    public String sayHello2(@RequestBody  String name);

}
