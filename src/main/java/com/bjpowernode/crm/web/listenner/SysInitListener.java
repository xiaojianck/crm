package com.bjpowernode.crm.web.listenner;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.util.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author:箫剑
 * 2019/6/18
 */
public class SysInitListener implements ServletContextListener {
    //该方法用来监听上下文域对象的创建，当上下文域对象创建了，立刻执行该监听方法

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext application=event.getServletContext();
        System.out.println("处理服务器缓存中的数据字典开始");

        DicService ds= (DicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> map=ds.getAll();
        Set<String> set =map.keySet();

        for (String key : set) {
            application.setAttribute(key, map.get(key));
        }
        System.out.println("处理服务器缓存中的数据字典结束");
    }
}
