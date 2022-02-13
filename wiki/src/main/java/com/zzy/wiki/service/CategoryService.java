package com.zzy.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzy.wiki.domain.Category;
import com.zzy.wiki.domain.CategoryExample;
import com.zzy.wiki.mapper.CategoryMapper;
import com.zzy.wiki.req.CategoryQueryReq;
import com.zzy.wiki.req.CategorySaveReq;
import com.zzy.wiki.resp.CategoryQueryResp;
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
public class CategoryService {
    
    //注入进来mapper    @Resource是jdk自带的    @Autowired是sprinboot自带的需要在接口上加入@Repository注入
    //    @Resource
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;

    public List<CategoryQueryResp> all(){
//        mybatis的固定写法
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        //列表复制
        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        return respList;
    }


    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
//        mybatis的固定写法
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        pageInfo.getTotal();
        pageInfo.getPages();
        log.info("总行数：{}",pageInfo.getTotal());
        log.info("总页数：{}",pageInfo.getPages());
//        List<CategoryResp> respList = new ArrayList<>();
//        for (Category category : categoryList) {
////            CategoryResp categoryResp = new CategoryResp();
////            BeanUtils.copyProperties(category,categoryResp);
        //对象赋值
//            CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class); ---单个赋值
//            respList.add(categoryResp);
//        }

        //列表复制
        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);


        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 保存
     */
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req,Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            //更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /**
     * 删除
     */
    public void delete(long id) {

        categoryMapper.deleteByPrimaryKey(id);

    }

}
