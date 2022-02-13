package com.zzy.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzy.wiki.req.UserLoginReq;
import com.zzy.wiki.req.UserQueryReq;
import com.zzy.wiki.req.UserResetPasswordReq;
import com.zzy.wiki.req.UserSaveReq;
import com.zzy.wiki.resp.CommonResp;
import com.zzy.wiki.resp.PageResp;
import com.zzy.wiki.resp.UserLoginResp;
import com.zzy.wiki.resp.UserQueryResp;
import com.zzy.wiki.service.UserService;
import com.zzy.wiki.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {



//    @Value("${test.hello}")
//    private String testHello;
    @Autowired
    private UserService userService;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(@Valid UserQueryReq req){
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@Valid @RequestBody UserSaveReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @RequestMapping(value = "/reset-password",method = RequestMethod.POST)
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable long id){
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

    /**
     * 登录
     * @param req
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResp login(@Valid @RequestBody UserLoginReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));

        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);

        Long token = snowFlake.nextId();
        log.info("生成单点登录token: {}，并放入redis中",token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);
        resp.setContent(userLoginResp);
        return resp;
    }


    /**
     * 清除登录信息
     * @return
     */
    @RequestMapping(value = "/logout/{token}",method = RequestMethod.GET)
    public CommonResp logout(@PathVariable String token){
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        log.info("从redis中删除token: {}",token);
        return resp;
    }
}
