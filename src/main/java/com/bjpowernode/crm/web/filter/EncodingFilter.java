package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Author:箫剑
 * 2019/6/14
 */
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到过滤字符编码的过滤器");
        //post请求中文参数的处理
        req.setCharacterEncoding("utf-8");
        //响应流响应中文信息的处理
        resp.setContentType("text/html;charset=utf-8");
        //处理完字符编码后，需要将请求放行
        chain.doFilter(req, resp);
    }
}
