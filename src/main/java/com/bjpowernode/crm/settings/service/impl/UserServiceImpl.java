package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:箫剑
 * 2019/6/14
 */
public class UserServiceImpl implements UserService {


    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String > map=new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd",loginPwd);
        UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
        User user=userDao.login(map);

        if (user==null){
            //为控制器及时作出错误通知，抛出自定义异常
            throw new LoginException("账号密码不正确");
        }
        //验证失效时间
        String expireTime=user.getExpireTime();
        String currentTime= DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw  new LoginException("账号已失效");
        }
        //验证锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw  new LoginException("账号已锁定");
        }
        //验证ip地址
        String allowIps=user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw  new LoginException("ip地址受限");
        }
//如果程序没有抛出任何异常信息，则执行return返回值，我们需要为控制器返回user对象
        return user;
    }

    @Override
    public List<User> getUserList() {
        UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
        List<User> users=userDao.getUserList();
        return users;
    }
}
