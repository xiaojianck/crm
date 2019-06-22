package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.PrintJson;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Author:箫剑
 * 2019/6/18
 */
public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到线索控制器");
        //取得访问路径
        String path = request.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(request, response);
        } else if ("/workbench/clue/saveClue.do".equals(path)) {
            saveClue(request, response);
        } else if ("/workbench/clue/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/clue/getActivityListByClueId.do".equals(path)) {
            getActivityListByClueId(request, response);
        } else if ("/workbench/clue/unbundById.do".equals(path)) {
            unbundbyId(request, response);
        } else if ("/workbench/clue/convert.do".equals(path)) {
            convert(request, response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到线索转换");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String clueId=request.getParameter("clueId");
        System.out.println(clueId);
        String flag =request.getParameter("flag");
        Tran t=null;

        //如果flag为A，证明需要创建交易
        if ("a".equals(flag)){
            t=new Tran();

            //接受交易表单中的数据
            String mongey = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate=request.getParameter("expectedDate");
            String stage=request.getParameter("stage");
            String activityId= request.getParameter("activityId");
            String createTime = DateTimeUtil.getSysTime();

            String id = UUIDUtil.getUUID();
            t.setId(id);
            t.setMoney(mongey);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);
        }

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        //需要为业务层传递那些参数
        boolean flag1=cs.convert(clueId,t,createBy);
        if (flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }


    }

    private void unbundbyId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入解除关连关系");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbundbyId(id);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id取得市场活动列表");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        String clueId = request.getParameter("clueId");

        List<Activity> activityList = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response, activityList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        String id = request.getParameter("id");
        Clue c = cs.getClueById(id);
        System.out.println(c.getId());
        request.setAttribute("c", c);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);


    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索添加操作");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String creatBy = ((User) request.getSession().getAttribute("user")).getName();

        Clue c = new Clue();
        c.setWebsite(website);
        c.setAppellation(appellation);
        c.setAddress(address);
        c.setOwner(owner);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setId(id);
        c.setCreateTime(createTime);
        c.setCreateBy(creatBy);
        c.setFullname(fullname);
        c.setCompany(company);
        boolean flag = cs.saveClue(c);
        System.out.println(flag);
        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询所有者信息");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = us.getUserList();
        PrintJson.printJsonObj(response, userList);


    }
}
