package com.zzy.wiki.mapper;

import com.zzy.wiki.domain.Test;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestMapper {

    public List<Test> list();

}
