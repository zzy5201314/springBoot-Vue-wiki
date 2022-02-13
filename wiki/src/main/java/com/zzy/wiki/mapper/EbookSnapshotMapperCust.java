package com.zzy.wiki.mapper;

import com.zzy.wiki.resp.StatisticResp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zzy
 * @date 2022/2/11
 */
@Repository
public interface EbookSnapshotMapperCust {

    public void genSnapshot();

    List<StatisticResp> getStatistic();

    List<StatisticResp> get30Statistic();
}
