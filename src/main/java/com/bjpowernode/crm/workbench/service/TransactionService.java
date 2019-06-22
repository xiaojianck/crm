package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

/**
 * Author:箫剑
 * 2019/6/20
 */
public interface TransactionService {
    List<String> getByName(String name);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);


    boolean changeStage(Tran t);

    int getTotal();

    List<Map<String, Object>> getList();
}
