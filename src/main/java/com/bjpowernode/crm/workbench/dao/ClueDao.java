package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;

public interface ClueDao {


    int saveClue(Clue c);

    Clue getClueById(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
