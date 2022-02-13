package com.zzy.wiki.service;

import com.zzy.wiki.domain.Demo;
import com.zzy.wiki.mapper.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {

    //注入进来mapper    @Resource是jdk自带的    @Autowired是sprinboot自带的需要在接口上加入@Repository注入
    //    @Resource
    @Autowired
    private DemoMapper demoMapper;

    public List<Demo> list(){
        return demoMapper.selectByExample(null);
    }
}
