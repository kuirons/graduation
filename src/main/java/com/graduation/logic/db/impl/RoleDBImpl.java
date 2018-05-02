package com.graduation.logic.db.impl;

import com.graduation.data.bean.RoleBean;
import com.graduation.data.extrabean.RoleData;
import com.graduation.logic.db.HbaseManager;
import com.graduation.security.Jurisdiction;
import com.graduation.util.CommonUtil;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Created by kuirons on 18-4-28 */
@Repository
public class RoleDBImpl {
  private static final String TABLE_NAME = "graduation_role";
  @Autowired HbaseManager hbaseManager;
  @Autowired RoleJurisdicationDBImpl roleJurisdicationDB;

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

  public List<RoleData> getRoleData() {
    List<RoleData> results = new ArrayList<>();
    List<RoleBean> roleBeans = getAllRoleInfos();
    roleBeans.forEach(
        roleBean -> {
          RoleData result = new RoleData();
          result.setRoleName(roleBean.getRole());
          result.setDescription(roleBean.getDescription());
          result.setJurisdictions(
              roleJurisdicationDB
                  .getPermissionByRoleName(roleBean.getRole())
                  .stream()
                  .map(Jurisdiction::toString)
                  .collect(Collectors.toList()));
          results.add(result);
        });
    return results;
  }
}
