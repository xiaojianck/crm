package com.bjpowernode.crm.web.listenner;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.util.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

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


        //处理阶段和可能性之间的对应关系
        /*

            将属性文件中的阶段和可能性之间的键值对关系,处理成为java中的键值对关系map
			将map保存到服务器缓存中

         */
        System.out.println("处理阶段和可能性之间的对应关系");

        ResourceBundle rb= ResourceBundle.getBundle("Stage2Possibility");
        Map<String,String> pMap=new HashMap<>();
        Enumeration<String> e=rb.getKeys();

        while (e.hasMoreElements()){
            String stage=e.nextElement();
            String possibility = rb.getString(stage);

            pMap.put(stage, possibility);

        }
        application.setAttribute("pMap", pMap);
    }
}
