package com.zzy.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzy.wiki.domain.User;
import com.zzy.wiki.domain.UserExample;
import com.zzy.wiki.exception.BusinessException;
import com.zzy.wiki.exception.BusinessExceptionCode;
import com.zzy.wiki.mapper.UserMapper;
import com.zzy.wiki.req.UserLoginReq;
import com.zzy.wiki.req.UserQueryReq;
import com.zzy.wiki.req.UserResetPasswordReq;
import com.zzy.wiki.req.UserSaveReq;
import com.zzy.wiki.resp.PageResp;
import com.zzy.wiki.resp.UserLoginResp;
import com.zzy.wiki.resp.UserQueryResp;
import com.zzy.wiki.util.CopyUtil;
import com.zzy.wiki.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Slf4j
public class UserService {
    
    //注入进来mapper    @Resource是jdk自带的    @Autowired是sprinboot自带的需要在接口上加入@Repository注入
    //    @Resource
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req){
//        mybatis的固定写法
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getLoginName())){
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        pageInfo.getTotal();
        pageInfo.getPages();
        log.info("总行数：{}",pageInfo.getTotal());
        log.info("总页数：{}",pageInfo.getPages());
//        List<UserResp> respList = new ArrayList<>();
//        for (User user : userList) {
////            UserResp userResp = new UserResp();
////            BeanUtils.copyProperties(user,userResp);
        //对象赋值
//            UserResp userResp = CopyUtil.copy(user, UserResp.class); ---单个赋值
//            respList.add(userResp);
//        }

        //列表复制
        List<UserQueryResp> respList = CopyUtil.copyList(userList, UserQueryResp.class);


        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 保存
     */
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            User userDB = selectByLoginName(req.getLoginName());
            if (ObjectUtils.isEmpty(userDB)) {
                // 新增
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
                // 用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            // 更新
            user.setLoginName(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 删除
     */
    public void delete(long id) {

        userMapper.deleteByPrimaryKey(id);

    }

    /**
     * 修改密码
     */
    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    /**
     * 登录
     */
    public UserLoginResp login(UserLoginReq req) {
        User userDb = selectByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(userDb)){
            //用户名不存在
            log.info("用戶名不存在, {}",req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else {
            if (userDb.getPassword().equals(req.getPassword())){
                //登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(userDb,UserLoginResp.class);
                return userLoginResp;
            }else {
                //密码不对
                log.info("密码不对,输入密码: {},数据库密码： {}",req.getLoginName(),userDb.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

}
