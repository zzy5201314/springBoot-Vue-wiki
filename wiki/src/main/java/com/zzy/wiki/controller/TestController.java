package com.zzy.wiki.controller;

import com.zzy.wiki.domain.Test;
import com.zzy.wiki.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

//    @Value("${test.hello}")
//    private String testHello;
    @Autowired
    private TestService testService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/hello")
    public String hello(){
        return "Hello Word!";
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Test> helloList(){
        return testService.list();
    }

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key,@PathVariable String value) {
        redisTemplate.opsForValue().set(key, value,3600, TimeUnit.SECONDS);
        log.info("key: {},value: {}",key,value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object Object = redisTemplate.opsForValue().get(key);
        log.info("key: {},value: {}",key, Object);
        return Object;
    }
}
