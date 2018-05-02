package com.graduation.logic.jurisdiction;

import com.graduation.data.bean.JurisdictionBean;
import com.graduation.logic.db.impl.JurisdictionDBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** Created by kuirons on 18-5-3 */
@Service
public class JurisdictionManager {
  @Autowired JurisdictionDBImpl jurisdictionDB;

  public List<JurisdictionBean> getAllPermissions() {
    return jurisdictionDB.getAllPermissions();
  }
}
