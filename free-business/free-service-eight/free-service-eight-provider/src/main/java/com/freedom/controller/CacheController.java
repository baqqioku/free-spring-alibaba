package com.freedom.controller;

import com.freedom.framework.redis.annotation.CacheExpire;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.freedom.framework.redis.consts.Time.*;


@RestController
@RequestMapping("/cache")
@Slf4j
@Api
public class CacheController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @CacheExpire(FOREVER)
    @Cacheable("my_cache")
    @GetMapping("cache")
    public String getCache(String arg) {
        return "hello" + arg;
    }

    @GetMapping("cache1")
    @CacheExpire(2 * DAY)
    @Cacheable("my_cache")
    public String getCache1(String arg1, String arg2) {
        return arg1 + arg2;
    }


    @CachePut("my_cache")
    @GetMapping("update")
    public String update(String arg) {
        return arg;
    }

    @CacheEvict("my_cache")
    @GetMapping("delete")
    public String delete(String arg) {
        return arg;
    }


}
