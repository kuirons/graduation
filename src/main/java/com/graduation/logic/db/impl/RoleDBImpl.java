package com.graduation.logic.db.impl;

import com.graduation.data.bean.RoleBean;
import com.graduation.logic.db.HbaseManager;
import com.graduation.util.CommonUtil;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/** Created by kuirons on 18-4-28 */
@Repository
public class RoleDBImpl {
  private static final String TABLE_NAME = "graduation_role";
  @Autowired HbaseManager hbaseManager;

  public List<RoleBean> getAllRoleInfos() {
    List<Result> results = hbaseManager.scanAllTable(TABLE_NAME);
    List<RoleBean> roleBeans = new ArrayList<>();
    results.forEach(
        result -> {
          RoleBean roleBean = new RoleBean();
          roleBean.setDescription(
              CommonUtil.bytesToString(
                  result.getValue("roleinfo".getBytes(), "description".getBytes())));
          roleBean.setRole(CommonUtil.bytesToString(result.getRow()));
          roleBeans.add(roleBean);
        });
    return roleBeans;
  }
}
