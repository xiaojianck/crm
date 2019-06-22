package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;

/**
 * Author:箫剑
 * 2019/6/18
 */
public interface ClueService {

    boolean saveClue(Clue c);

    Clue getClueById(String id);


    boolean unbundbyId(String id);

    boolean convert(String clueId, Tran t, String createBy);
}
