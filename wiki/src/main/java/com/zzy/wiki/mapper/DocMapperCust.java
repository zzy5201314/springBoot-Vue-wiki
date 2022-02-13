package com.zzy.wiki.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author zzy
 * @date 2022/2/8
 */
@Repository
public interface DocMapperCust {

    public void increaseViewCount(@Param("id") Long id);

    public void increaseVoteCount(@Param("id") Long id);

    public void updateEbookInfo();
}
