package com.zzy.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzy.wiki.domain.Ebook;
import com.zzy.wiki.domain.EbookExample;
import com.zzy.wiki.mapper.EbookMapper;
import com.zzy.wiki.req.EbookQueryReq;
import com.zzy.wiki.req.EbookSaveReq;
import com.zzy.wiki.resp.EbookQueryResp;
import com.zzy.wiki.resp.PageResp;
import com.zzy.wiki.util.CopyUtil;
import com.zzy.wiki.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Slf4j
public class EbookService {
    
    //注入进来mapper    @Resource是jdk自带的    @Autowired是sprinboot自带的需要在接口上加入@Repository注入
    //    @Resource
    @Autowired
    private EbookMapper ebookMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> list(EbookQueryReq req){
//        mybatis的固定写法
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%" + req.getName() + "%");
        }
        if(!ObjectUtils.isEmpty(req.getCategoryId2())){
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        pageInfo.getTotal();
        pageInfo.getPages();
        log.info("总行数：{}",pageInfo.getTotal());
        log.info("总页数：{}",pageInfo.getPages());
//        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
////            EbookResp ebookResp = new EbookResp();
////            BeanUtils.copyProperties(ebook,ebookResp);
        //对象赋值
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class); ---单个赋值
//            respList.add(ebookResp);
//        }

        //列表复制
        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);


        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 保存
     */
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req,Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        } else {
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    /**
     * 删除
     */
    public void delete(long id) {

        ebookMapper.deleteByPrimaryKey(id);

    }

}
