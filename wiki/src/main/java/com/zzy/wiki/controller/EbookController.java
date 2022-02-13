package com.zzy.wiki.controller;

import com.zzy.wiki.req.EbookQueryReq;
import com.zzy.wiki.req.EbookSaveReq;
import com.zzy.wiki.resp.CommonResp;
import com.zzy.wiki.resp.EbookQueryResp;
import com.zzy.wiki.resp.PageResp;
import com.zzy.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {

//    @Value("${test.hello}")
//    private String testHello;
    @Autowired
    private EbookService ebookService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(@Valid EbookQueryReq req){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@Valid @RequestBody EbookSaveReq req){
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable long id){
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
