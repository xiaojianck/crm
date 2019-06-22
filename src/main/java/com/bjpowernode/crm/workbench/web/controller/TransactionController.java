package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.PrintJson;
import com.bjpowernode.crm.util.ServiceFactory;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TransactionService;
import com.bjpowernode.crm.workbench.service.impl.TransactionServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:箫剑
 * 2019/6/20
 */
public class TransactionController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到交易控制器");

        //取得访问路径
        String path = request.getServletPath();

        if ("/workbench/transaction/getCustomerByName.do".equals(path)) {
            getCustomerByName(request, response);
        }if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request, response);
        }if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)) {
            getHistoryListByTranId(request, response);
        }if ("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request, response);
        }if ("/workbench/transaction/getCharts.do".equals(path)) {
            getCharts(request, response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到交易图表的操作");
        TransactionService ts= (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        int total = ts.getTotal();
        TransactionService ts2= (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        List<Map<String ,Object>> mapList=ts2.getList();
        Map<String,Object> map=new HashMap<>();
        map.put("total", total);
        map.put("dataList", mapList);
        PrintJson.printJsonObj(response, map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改交易阶段的操作");
        String id=request.getParameter("id");
        String stage=request.getParameter("stage");
        String money=request.getParameter("money");
        String expectedDate=request.getParameter("expectedDate");
        System.out.println(expectedDate);
        String editBy=((User)request.getSession().getAttribute("user")).getName();
        String editTime= DateTimeUtil.getSysTime();
        System.out.println(editTime);
        TransactionService ts= (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Tran t=new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        boolean flag=ts.changeStage(t);
        Map<String,String> pMap= (Map<String, String>) this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("t", t);
        PrintJson.printJsonObj(response, map);

    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询交易历史操作");
        String tranId= request.getParameter("tranId");
        TransactionService ts= (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());

        List<TranHistory> tranHistoryList=ts.getHistoryListByTranId(tranId);

        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");

        for(TranHistory th:tranHistoryList){
            th.setPossibility(pMap.get(th.getStage()));
        }
        PrintJson.printJsonObj(response, tranHistoryList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        System.out.println("进入到交易详细信息页");
        String id=request.getParameter("id");
        TransactionService ts= (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Tran t=ts.detail(id);

        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");

        t.setPossibility(pMap.get(t.getStage()));

        request.setAttribute("t", t);

        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    private void getCustomerByName(HttpServletRequest request, HttpServletResponse response) {
        TransactionService ts = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        String name = request.getParameter("name");
        List<String> name1= ts.getByName(name);
        PrintJson.printJsonObj(response, name1);
    }
}
