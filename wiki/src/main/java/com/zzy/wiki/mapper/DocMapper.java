package com.zzy.wiki.mapper;

import com.zzy.wiki.domain.Doc;
import com.zzy.wiki.domain.DocExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocMapper {
    long countByExample(DocExample example);

    int deleteByExample(DocExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Doc record);

    int insertSelective(Doc record);

    List<Doc> selectByExample(DocExample example);

    Doc selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Doc record, @Param("example") DocExample example);

    int updateByExample(@Param("record") Doc record, @Param("example") DocExample example);

    int updateByPrimaryKeySelective(Doc record);

    int updateByPrimaryKey(Doc record);
}