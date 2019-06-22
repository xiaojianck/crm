package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TransactionService;

import java.util.List;
import java.util.Map;

/**
 * Author:箫剑
 * 2019/6/20
 */
public class TransactionServiceImpl implements TransactionService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> mapList=tranDao.getList();
        return mapList;
    }

    @Override
    public int getTotal() {
        int total=tranDao.getTotal();
        return total;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean falg = true;
        int count1 = tranDao.changeStage(t);
        if (count1 != 1) {
            falg = false;
        }

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getEditBy());
        System.out.println(th.getCreateBy());
        th.setTranId(t.getId());
        System.out.println();
        int count2 = tranHistoryDao.save(th);
        if (count2 != 1) {
            falg = false;
        }

        return falg;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> tranHistoryList = tranHistoryDao.getHistoryListByTranId(tranId);
        return tranHistoryList;
    }

    @Override
    public Tran detail(String id) {
        Tran t = tranDao.detail(id);
        return t;
    }

    @Override
    public List<String> getByName(String name) {
        List<String> arrays = customerDao.getByCusName(name);
        return arrays;
    }
}
