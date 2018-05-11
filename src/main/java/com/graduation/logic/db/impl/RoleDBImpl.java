package com.graduation.logic.db.impl;

import com.graduation.data.bean.RoleBean;
import com.graduation.data.extrabean.RoleData;
import com.graduation.logic.db.HbaseManager;
import com.graduation.security.Jurisdiction;
import com.graduation.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Put;
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

  public boolean updateRoleInfos(String changeRoleName, String newRoleDescription) {
    return addRoleInfos(newRoleDescription, changeRoleName);
  }

  public boolean addRoleInfos(String roleDescription, String roleName) {
    if (StringUtils.isBlank(roleDescription) || StringUtils.isBlank(roleName)) return false;
    Put put = new Put(roleName.getBytes());
    put.addColumn("roleinfo".getBytes(), "description".getBytes(), roleDescription.getBytes());
    hbaseManager.synPut(TABLE_NAME, put);
    return true;
  }

  public void deleteRole(String deleteName) {
    hbaseManager.delete(TABLE_NAME, deleteName);
  }

  public RoleData getRoleDataByName(String roleName) {
    // todo 提供模糊搜索
    if (StringUtils.isBlank(roleName)) return null;
    RoleData roleData = new RoleData();
    Result roleinfo = hbaseManager.getRow(TABLE_NAME, roleName);
    List<Jurisdiction> permission = roleJurisdicationDB.getPermissionByRoleName(roleName);
    roleData.setRoleName(roleName);
    if (roleinfo.isEmpty()) return null;
    String description =
        CommonUtil.bytesToString(
            roleinfo.getValue("roleinfo".getBytes(), "description".getBytes()));
    if (StringUtils.isBlank(description)) return null;
    roleData.setDescription(description);
    roleData.setJurisdictions(
        permission.stream().map(Jurisdiction::toString).collect(Collectors.toList()));
    return roleData;
  }
}
