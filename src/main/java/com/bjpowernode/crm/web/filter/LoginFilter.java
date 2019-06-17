package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author:箫剑
 * 2019/6/14
 */
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chian) throws IOException, ServletException {
        System.out.println("进入到验证是否已经登录过的过滤器");

        HttpServletRequest request= (HttpServletRequest) req;
        HttpServletResponse response= (HttpServletResponse) resp;
        //取得访问路径
        String path=request.getServletPath();
        //如果是登录页和登录验证请求，则将请求自动放行
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)) {
            chian.doFilter(req, resp);
        }else {
            //其他请求，正常验证有没有登录过
            User user= (User) request.getSession().getAttribute("user");
            //如果user不为null说明登录过
            if (user!=null){
                //将请求放行到目标资源
                chian.doFilter(req, resp);
            }else {
                //如果user为null说明没登录过
                //重定向到登录页
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }
    }
}
