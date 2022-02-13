package com.zzy.wiki.controller;

import com.zzy.wiki.req.DocQueryReq;
import com.zzy.wiki.req.DocSaveReq;
import com.zzy.wiki.resp.DocQueryResp;
import com.zzy.wiki.resp.CommonResp;
import com.zzy.wiki.resp.PageResp;
import com.zzy.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

//    @Value("${test.hello}")
//    private String testHello;
    @Autowired
    private DocService docService;

    @RequestMapping(value = "/all/{ebookId}",method = RequestMethod.GET)
    public CommonResp all(@PathVariable Long ebookId){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(@Valid DocQueryReq req){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @RequestMapping(value = "/delete/{idsStr}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable String idsStr){
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return resp;
    }

    @RequestMapping(value = "/find-content/{id}",method = RequestMethod.GET)
    public CommonResp findContent(@PathVariable Long id){
        CommonResp<String> resp = new CommonResp<>();
        String content = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }

    //点赞
    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id){
        CommonResp commonResp = new CommonResp();
        docService.vote(id);
        return commonResp;
    }
}
