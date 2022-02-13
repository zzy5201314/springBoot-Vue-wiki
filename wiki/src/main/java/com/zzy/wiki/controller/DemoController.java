package com.zzy.wiki.controller;

import com.zzy.wiki.domain.Demo;
import com.zzy.wiki.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoController {

//    @Value("${test.hello}")
//    private String testHello;
    @Autowired
    private DemoService demoService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Demo> helloList(){
        return demoService.list();
    }
}
