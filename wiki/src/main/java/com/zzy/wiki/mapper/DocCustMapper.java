package com.zzy.wiki.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocCustMapper {

    public void increaseViewCount(@Param("id") Long id);

    public void increaseVoteCount(@Param("id") Long id);
}
