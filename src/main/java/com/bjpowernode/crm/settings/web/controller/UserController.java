package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.MD5Util;
import com.bjpowernode.crm.util.PrintJson;
import com.bjpowernode.crm.util.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:箫剑
 * 2019/6/14
 */
public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");

        //取得访问路径
        String path=request.getServletPath();
        System.out.println(path);
        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if ("/settings/user/xxx.do".equals(path)){
            //xxx(request,response);
        }

    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("处理验证登陆操作");
        String loginAct =request.getParameter("loginAct");
        String loginPwd =request.getParameter("loginPwd");
        //将密码解析为MD5的密文格式
        loginPwd= MD5Util.getMD5(loginPwd);
        //接收浏览器端的ip地址
        String ip=request.getRemoteAddr();
        System.out.println("ip="+ip);

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            User user =us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user", user);
            PrintJson.printJsonFlag(response, true);
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            //取得异常信息
            Map<String,Object> map=new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }
    }
}
