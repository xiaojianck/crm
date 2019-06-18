package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemak;

import java.util.List;

/**
 * Author:箫剑
 * 2019/6/14
 */
public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemak> getRemarkById(String activityId);

    int deleteRemark(String id);

    int saveRemark(ActivityRemak ar);

    int updateRemark(ActivityRemak ar);
}
