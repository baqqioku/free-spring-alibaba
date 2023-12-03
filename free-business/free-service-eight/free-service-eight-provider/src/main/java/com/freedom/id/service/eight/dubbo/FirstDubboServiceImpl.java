package com.freedom.id.service.eight.dubbo;


import com.freedom.eight.api.SecondService;
import com.freedom.model.Guoguo;
import com.freedom.model.mapper.GuoguoMapper;
import com.freedom.id.service.eight.api.FristDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DubboService(cluster = "failfast")
public class FirstDubboServiceImpl implements FristDubboService {

    @DubboReference(retries = 0, timeout = 5000, cluster = "failfast",url = "dubbo://127.0.0.1:20882",check = false)
    private SecondService secondDubboService;

    @Autowired
    GuoguoMapper guoguoMapper;

    @Override
    @Transactional
    public String sayHello(String name) {
        Guoguo guoguo = new Guoguo();
        guoguo.setName(name);
        guoguoMapper.insertSelective(guoguo);
        return name + secondDubboService.secondSyaHello(name);
    }
}
