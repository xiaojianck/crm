package com.bjpowernode.crm.workbench.dao;

/**
 * Author:箫剑
 * 2019/6/14
 */
public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);
}
