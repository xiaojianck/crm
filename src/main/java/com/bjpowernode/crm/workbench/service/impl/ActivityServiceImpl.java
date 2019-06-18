package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVo;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemak;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:箫剑
 * 2019/6/14
 */
public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public boolean save(Activity a) {
        boolean flag = true;
        int count = activityDao.save(a);
        if ((count != 1)) {
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {
        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);
        //取得total
        int total = activityDao.getTotalByCondition(map);
        //创建一个vo对象，
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setDataList(dataList);
        vo.setTotal(total);

        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        int count1 = activityRemarkDao.getCountByAids(ids);

        int count2 = activityRemarkDao.deleteByAids(ids);

        if (count1 != count2) {
            flag = false;
        }

        //再删除市场活动本身
        int count3 = activityDao.delete(ids);
        if (count3 != ids.length) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        //取得uList
        List<User> uList = userDao.getUserList();
//        System.out.println(uList.toString());
        //取得a
        Activity a = activityDao.getById(id);
//        System.out.println(a.toString());
        Map<String, Object> map = new HashMap<>();
        map.put("uList", uList);
        map.put("a", a);
        //返回map

        return map;
    }

    @Override
    public boolean update(Activity a) {
        boolean flag = true;
        int count = activityDao.update(a);
        if ((count != 1)) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        Activity a = activityDao.getDetailById(id);
        return a;
    }

    @Override
    public List<ActivityRemak> getRemarkById(String activityId) {
        ActivityRemarkDao ar = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        List<ActivityRemak> remakList = ar.getRemarkById(activityId);
        return remakList;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;
        ActivityRemarkDao ar = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        int count = ar.deleteRemark(id);
        if (count == 0) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemak ar) {
        boolean flag=true;
        ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        int count=activityRemarkDao.saveRemark(ar);
        if (count==0){
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemak ar) {
        ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        boolean flag=true;
        int count=activityRemarkDao.updateRemark(ar);
        if (count==0){
            flag=false;
        }
        return flag;
    }
}
