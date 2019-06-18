package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;

/**
 * Author:箫剑
 * 2019/6/18
 */
public interface DicValueDao {
    List<DicValue> getDicValueListByDicType(String code);
}
