package com.freedom.controller;

import com.freedom.config.EightConfig;
import com.freedom.id.service.eight.api.FristDubboService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EightController {

    @Autowired
    FristDubboService fristDubboService;

    @Autowired
    EightConfig eightConfig;

    @GetMapping("/test/{name}")
    @Transactional
    public String test(@PathVariable String name){
        log.info(eightConfig.toString());
        return fristDubboService.sayHello(name);
    }


}
