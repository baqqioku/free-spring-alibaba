package com.freedom.second.provider;

import com.freedom.second.api.SecondApi;
import com.freedom.third.api.ThirdApi;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SecondProvider implements SecondApi {

    private Logger logger = LoggerFactory.getLogger(SecondProvider.class);

    @Autowired
    private ThirdApi third;

    @Override
    public String sayHello(String name) {
        logger.info("hello {}", name);
        third.sayHello(name);
        return name;
    }
}
