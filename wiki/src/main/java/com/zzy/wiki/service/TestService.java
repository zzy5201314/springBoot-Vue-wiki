package com.zzy.wiki.service;

import com.zzy.wiki.domain.Test;
import com.zzy.wiki.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    //注入进来mapper    @Resource是jdk自带的    @Autowired是sprinboot自带的需要在接口上加入@Repository注入
    //    @Resource
    @Autowired
    private TestMapper testMapper;

    public List<Test> list(){
        return testMapper.list();
    }
}
