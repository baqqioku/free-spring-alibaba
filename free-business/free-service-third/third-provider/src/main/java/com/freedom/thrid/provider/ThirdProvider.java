package com.freedom.thrid.provider;

import com.freedom.third.api.ThirdApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ThirdProvider implements ThirdApi {

    private Logger logger = LoggerFactory.getLogger(ThirdProvider.class);

    @Override
    public String sayHello(String name) {
        logger.info("hello {}", name);
        return name;
    }
}
