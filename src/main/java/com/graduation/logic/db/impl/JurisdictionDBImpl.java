package com.graduation.logic.db.impl;

import com.graduation.data.bean.JurisdictionBean;
import com.graduation.logic.db.HbaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Created by kuirons on 18-4-7 */
@Repository
public class JurisdictionDBImpl {
  @Autowired HbaseManager hbaseManager;


    public List<String> getJurisdicByRoleName(String role) {
        return null;
    }
}
