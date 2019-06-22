package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.util.DateTimeUtil;
import com.bjpowernode.crm.util.SqlSessionUtil;
import com.bjpowernode.crm.util.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.List;

/**
 * Author:箫剑
 * 2019/6/18
 */
public class ClueServiceImpl implements ClueService {
    //线索相关的表操作
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    //客户相关的表操作
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //联系人相关的表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    //交易相关表操作
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;

        System.out.println("转换线索的业务流程开始");
        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue c = clueDao.getById(clueId);
        System.out.println(c);
        //(2) 通过线索对象提取客户名称（公司名称），当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = c.getCompany();
        Customer cus = customerDao.getByName(company);
        //如果cus为null，说明没有查询到该客户，需要创建一个新客户
        if (cus == null) {
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateTime(createTime);
            cus.setCreateBy(createBy);
            cus.setContactSummary(c.getContactSummary());
            cus.setAddress(c.getAddress());


            //添加客户

            int count1 = customerDao.save(cus);
            if (count1 != 1) {
                flag = false;
            }

        }
        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setSource(c.getSource());
        con.setId(UUIDUtil.getUUID());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        con.setContactSummary(c.getContactSummary());
        con.setAddress(c.getAddress());
        con.setAppellation(c.getAppellation());

        //添加联系人
        int count2 = contactsDao.save(con);
        if (count2 != 1) {
            flag = false;
        }
        //-------------------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------
        //以上第2步，我们处理完了客户，将来在使用到客户的id的时候，我们使用cus.getId()
        //以上第3步，我们处理完了联系人，将来在使用到联系人的id的时候，我们使用con.getId()
        //-------------------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------------------

        //(4) 线索备注转换到客户备注以及联系人备注
        //查询与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList) {
            //取得需要转换的备注信息
            String noteContent = clueRemark.getNoteContent();

            //处理客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);

            //添加客户备注
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 != 1) {
                flag = false;
            }


            //处理联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            //添加联系人备注
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 != 1) {
                flag = false;
            }

        }
        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //查询出与该线索相关的线索市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {

            //取出每一条关联关系，通过关联关系取出activityId
            //将activityId和contactsId做新的关联
            String activityId = clueActivityRelation.getActivityId();

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());

            //添加联系人和市场活动的关联关系
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 != 1) {
                flag = false;
            }
        }
        //(6)如果有创建交易需求，创建一条交易
        if (t != null) {
            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setNextContactTime(c.getNextContactTime());
            t.setContactsId(con.getId());
            //添加交易
            int count6 = tranDao.save(t);
            if (count6 != 1) {
                flag = false;
            }

            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setExpectedDate(t.getExpectedDate());
            th.setMoney(t.getMoney());
            th.setStage(t.getStage());
            th.setTranId(t.getId());

            //添加交易历史
            int count7 = tranHistoryDao.save(th);
            if (count7 != 1) {
                flag = false;
            }
        }
        //(8) 删除线索备注
        for (ClueRemark clueRemark : clueRemarkList) {
            int count8 = clueRemarkDao.delete(clueRemark);
            if (count8 != 1) {
                flag = false;
            }
        }
        //(9) 删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if (count9 != 1) {
                flag = false;
            }
        }
        //(10) 删除线索
        int count10 = clueDao.delete(clueId);
        if (count10 != 10) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean unbundbyId(String id) {
        ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
        boolean flag = true;
        int count = clueActivityRelationDao.unbundbyId(id);
        if (count == 0) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Clue getClueById(String id) {
        Clue c = clueDao.getClueById(id);
        return c;
    }

    @Override
    public boolean saveClue(Clue c) {
        boolean flag = true;
        int count = clueDao.saveClue(c);
        System.out.println(count);
        if (count == 0) {
            flag = false;
        }
        return flag;
    }
}
