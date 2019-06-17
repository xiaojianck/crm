package com.bjpowernode.crm.vo;

import java.util.List;

/**
 * Author:箫剑
 * 2019/6/15
 */
public class PaginationVo<T> {
    private List<T> dataList;
    private int total;

    public List<T> getDataList() {
        return dataList;
    }

    public PaginationVo<T> setDataList(List<T> dataList) {
        this.dataList = dataList;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public PaginationVo<T> setTotal(int total) {
        this.total = total;
        return this;
    }
}
