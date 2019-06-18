package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemak;

import java.util.List;
import java.util.Map;

/**
 * Author:箫剑
 * 2019/6/14
 */
public interface ActivityService {
    boolean save(Activity a);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] id);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemak> getRemarkById(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemak ar);

    boolean updateRemark(ActivityRemak ar);
}

