package com.freedom.thrid.provider;

import com.freedom.model.Guoguo;
import com.freedom.model.mapper.GuoguoMapper;
import com.freedom.third.api.ThirdApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ThirdProvider implements ThirdApi {

    private Logger logger = LoggerFactory.getLogger(ThirdProvider.class);

    @Autowired
    private GuoguoMapper guoguoMapper;

    @Override
    public String sayHello(String name) {
        logger.info("hello {}", name);
        return name;
    }

    @Override
    @Transactional
    public String sayHello1(String name) {

        Guoguo guoguo = new Guoguo();
        guoguo.setName(name);
        System.out.println(1/0);
        return name;
    }
}
