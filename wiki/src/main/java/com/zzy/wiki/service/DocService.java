package com.zzy.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzy.wiki.domain.Content;
import com.zzy.wiki.domain.Doc;
import com.zzy.wiki.domain.DocExample;
import com.zzy.wiki.exception.BusinessException;
import com.zzy.wiki.exception.BusinessExceptionCode;
import com.zzy.wiki.mapper.ContentMapper;
import com.zzy.wiki.mapper.DocCustMapper;
import com.zzy.wiki.mapper.DocMapper;
import com.zzy.wiki.mapper.DocMapperCust;
import com.zzy.wiki.req.DocQueryReq;
import com.zzy.wiki.req.DocSaveReq;
import com.zzy.wiki.resp.DocQueryResp;
import com.zzy.wiki.resp.PageResp;
import com.zzy.wiki.util.CopyUtil;
import com.zzy.wiki.util.RedisUtil;
import com.zzy.wiki.util.RequestContext;
import com.zzy.wiki.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Slf4j
public class DocService {
    
    //注入进来mapper    @Resource是jdk自带的    @Autowired是sprinboot自带的需要在接口上加入@Repository注入
    //    @Resource
    @Autowired
    private DocMapper docMapper;

    @Autowired
    private DocCustMapper docCustMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private DocMapperCust docMapperCust;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WsService wsService;

//    @Resource
//    private RocketMQTemplate rocketMQTemplate;

    public List<DocQueryResp> all(Long ebookId){
//        mybatis的固定写法
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        //列表复制
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);

        return respList;
    }


    public PageResp<DocQueryResp> list(DocQueryReq req){
//        mybatis的固定写法
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        pageInfo.getTotal();
        pageInfo.getPages();
        log.info("总行数：{}",pageInfo.getTotal());
        log.info("总页数：{}",pageInfo.getPages());
//        List<DocResp> respList = new ArrayList<>();
//        for (Doc doc : docList) {
////            DocResp docResp = new DocResp();
////            BeanUtils.copyProperties(doc,docResp);
        //对象赋值
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class); ---单个赋值
//            respList.add(docResp);
//        }

        //列表复制
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);


        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 保存
     */
    @Transactional
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req,Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //更新
            docMapper.updateByPrimaryKey(doc);

            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0){
                contentMapper.insert(content);
            }
        }
    }

    /**
     * 删除
     */
    public void delete(long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    /**
     * 删除
     */
    public void delete(List<String> ids) {

        //        mybatis的固定写法
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);

    }

    /**
     * 查找大文本Content
     */
    public String findContent(Long id) {
       Content content = contentMapper.selectByPrimaryKey(id);
       //文档阅读数加一
        docCustMapper.increaseViewCount(id);
       if (ObjectUtils.isEmpty(content) || content == null) {
            content.setContent("");
        }
       return content.getContent();
    }

    //点赞
    public void vote (Long id){
//        docCustMapper.increaseVoteCount(id);
//        远程IP+doc.id作为key，24小时不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 5000)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        //推送消息
        Doc docDb = docMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");
        wsService.sendInfo("【"+docDb.getName()+"】被点赞！",logId);
//        rocketMQTemplate.convertAndSend("VOTE_TOPIC","【"+docDb.getName()+"】被点赞！");
    }




    public void updateEbookInfo(){
        docMapperCust.updateEbookInfo();
    }
}
