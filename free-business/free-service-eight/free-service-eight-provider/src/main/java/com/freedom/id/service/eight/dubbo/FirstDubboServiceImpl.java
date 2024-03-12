package com.freedom.id.service.eight.dubbo;


import com.alibaba.fastjson.JSON;
import com.freedom.eight.api.SecondService;
import com.freedom.id.service.eight.api.vo.UserVo;
import com.freedom.model.Guoguo;
import com.freedom.model.User;
import com.freedom.model.mapper.GuoguoMapper;
import com.freedom.id.service.eight.api.FristDubboService;
import com.freedom.model.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import javax.cache.annotation.CacheRemove;

@DubboService(cluster = "failfast")
public class FirstDubboServiceImpl implements FristDubboService {

    @DubboReference(retries = 0, timeout = 5000, cluster = "failfast",/*url = "dubbo://127.0.0.1:20882",*/check = false)
    private SecondService secondDubboService;

    @Autowired
    GuoguoMapper guoguoMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private FristDubboService fristDubboService;

    @Override
    @Transactional
    public String sayHello(String name) {
        Guoguo guoguo = new Guoguo();
        guoguo.setName(name);
        guoguoMapper.insertSelective(guoguo);
        return name + secondDubboService.secondSyaHello(name);
    }

    @Override
    public String findGuoguo(Long id) {
        Guoguo guoguo  = guoguoMapper.selectByPrimaryKey(id)  ;
        return JSON.toJSONString(guoguo);
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public UserVo findUser(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        String userJon = JSON.toJSONString(user);
        if(user == null){
            return null;
        }
        UserVo userVo = JSON.parseObject(userJon, UserVo.class);
        return userVo;
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    public Integer deleteUser(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }
}
