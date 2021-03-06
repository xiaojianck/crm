package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.*;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemak;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:箫剑
 * 2019/6/14
 */
public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");

        //取得访问路径
        String path=request.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request,response);
        }else if ("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }else if ("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/activity/getRemarkById.do".equals(path)){
            getRemarkById(request,response);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if ("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }

    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入更新备注操作");
        String id=request.getParameter("id");
        String noteContent=request.getParameter("noteContent");
        String editTime=DateTimeUtil.getSysTime();
        String editBy=((User)request.getSession().getAttribute("user")).getName();
        String editFlag="1";
        ActivityRemak ar=new ActivityRemak();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditFlag(editFlag);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag=as.updateRemark(ar);
        System.out.println(flag);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response, map);



    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入添加备注操作");
        String noteContent=request.getParameter("noteContent");
        System.out.println(noteContent);
        String activityId=request.getParameter("activityId");
        String id=UUIDUtil.getUUID();
        String createTime=DateTimeUtil.getSysTime();
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String editFlag="0";
        ActivityRemak ar =new ActivityRemak();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag=as.saveRemark(ar);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response, map);

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到删除备注操作");
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag=as.deleteRemark(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getRemarkById(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到备注信息搜索页");
        String activityId=request.getParameter("activityId");
        System.out.println(activityId);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemak> remakList=as.getRemarkById(activityId);
        PrintJson.printJsonObj(response, remakList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场活动的详细信息页");
        String id=request.getParameter("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a=as.detail(id);

        request.setAttribute("a", a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //修改时间：当前系统时间
        String editTime= DateTimeUtil.getSysTime();
        //修改人：当前登录的用户的名字
        String editBy=((User)request.getSession().getAttribute("user")).getName();
        Activity a=new Activity();
        a.setId(id);
        a.setCost(cost);
        a.setOwner(owner);
        a.setEditBy(editBy);
        a.setEditBy(editTime);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setDescription(description);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.update(a);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户列表+市场活动单条记录");
        String id=request.getParameter("id");
        System.out.println(id);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map=as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response, map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动操作");
        String[] id=request.getParameterValues("id");
                ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
                boolean flag = as.delete(id);
                PrintJson.printJsonFlag(response, flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动信息列表（结合分页查询+条件查询）");

        String name=request.getParameter("name");
        String owner=request.getParameter("owner");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        //页码
        String pageNoStr=request.getParameter("pageNo");
        //每页展示的记录数
        String pageSizeStr=request.getParameter("pageSize");

        int pageNo=Integer.valueOf(pageNoStr);
        int pageSize=Integer.valueOf(pageSizeStr);
        //计算略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        //创建一个Map对象，保存这六个数据
        Map<String,Object> map=new HashMap<>();
        map.put("name", name);
        map.put("owner",owner );
        map.put("startDate",startDate );
        map.put("endDate", endDate);
        map.put("skipCount",skipCount );
        map.put("pageSize",pageSize );
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
      PaginationVo<Activity> vo=as.pageList(map);
      PrintJson.printJsonObj(response, vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的添加操作");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //创建时间：当前系统时间
        String createTime= DateTimeUtil.getSysTime();
        //创建人：当前登录的用户的名字
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        Activity a=new Activity();
        a.setId(id);
        a.setCost(cost);
        a.setOwner(owner);
        a.setCreateBy(createBy);
        a.setCreateTime(createTime);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setDescription(description);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.save(a);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息");
        UserService user= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList=user.getUserList();
        PrintJson.printJsonObj(response, userList);
    }
}
