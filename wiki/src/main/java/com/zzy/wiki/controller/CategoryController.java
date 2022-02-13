package com.zzy.wiki.controller;

import com.zzy.wiki.req.CategoryQueryReq;
import com.zzy.wiki.req.CategorySaveReq;
import com.zzy.wiki.resp.CommonResp;
import com.zzy.wiki.resp.CategoryQueryResp;
import com.zzy.wiki.resp.PageResp;
import com.zzy.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

//    @Value("${test.hello}")
//    private String testHello;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public CommonResp all(){
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all();
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(@Valid CategoryQueryReq req){
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@Valid @RequestBody CategorySaveReq req){
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable long id){
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
