package com.freedom.second.provider;

import com.freedom.model.Guoguo;
import com.freedom.model.User;
import com.freedom.model.mapper.GuoguoMapper;
import com.freedom.model.mapper.UserMapper;
import com.freedom.second.api.SecondApi;
import com.freedom.second.api.ao.FirstApi;
import com.freedom.second.api.ao.FirstEnum;
import com.freedom.third.api.ThirdApi;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SecondProvider implements SecondApi {

    private Logger logger = LoggerFactory.getLogger(SecondProvider.class);

    @Autowired
    private ThirdApi third;

    @Autowired
    private GuoguoMapper guoguoMapper;

    @Override
    public String sayHello(String name) {
        logger.info("hello {}", name);
        third.sayHello(name);
        return name;
    }

    @Override
    public FirstApi sayHello1(String name) {
        FirstApi  api = new FirstApi();
        api.setName("guoguo");
        api.setFirstEnum(FirstEnum.ONE);
        return api;
    }

    @Override
    @Transactional
    public String sayHello2(String name) {
        Guoguo guoguo = new Guoguo();
        guoguo.setName(name);
        guoguoMapper.insertSelective(guoguo);
        third.sayHello1(name);
        return name;
    }
}
