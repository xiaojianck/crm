package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * Author:箫剑
 * 2019/6/18
 */
public interface DicService {
    Map<String, List<DicValue>> getAll();
}
