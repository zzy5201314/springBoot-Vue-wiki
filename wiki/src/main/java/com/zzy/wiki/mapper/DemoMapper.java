package com.zzy.wiki.mapper;

import com.zzy.wiki.domain.Demo;
import com.zzy.wiki.domain.DemoExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemoMapper {
    long countByExample(DemoExample example);

    int deleteByExample(DemoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Demo record);

    int insertSelective(Demo record);

    List<Demo> selectByExample(DemoExample example);

    Demo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Demo record, @Param("example") DemoExample example);

    int updateByExample(@Param("record") Demo record, @Param("example") DemoExample example);

    int updateByPrimaryKeySelective(Demo record);

    int updateByPrimaryKey(Demo record);
}