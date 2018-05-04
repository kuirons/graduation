package com.graduation.logic.db.impl;

import com.graduation.data.bean.RoleJurisdicationBean;
import com.graduation.logic.db.HbaseManager;
import com.graduation.security.Jurisdiction;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WuGYu
 * @date 2018/4/8 15:02
 */
@Repository
public class RoleJurisdicationDBImpl {
  private static final String TABLENAME = "graduation_role_jurisdiction";
  @Autowired HbaseManager hbaseManager;

  public List<Jurisdiction> getPermissionByRoleNames(List<String> roleNames) {
    List<Jurisdiction> all = new ArrayList<>();
    roleNames.forEach(
        roleName -> {
          List<Jurisdiction> jurisdictions = getPermissionByRoleName(roleName);
          // 如果不是枚举类型或者基础数据类型，使用下面的方式需要谨慎！！！！！！噢啦噢啦噢啦
          all.removeAll(jurisdictions);
          all.addAll(jurisdictions);
        });
    return all;
  }

  public List<Jurisdiction> getPermissionByRoleName(String role) {
    List<Jurisdiction> jurisdictions = new ArrayList<>();
    PrefixFilter filter = new PrefixFilter(role.getBytes());
    ResultScanner results = hbaseManager.getScan(TABLENAME, null, filter);
    results.forEach(
        result -> {
          RoleJurisdicationBean bean = new RoleJurisdicationBean(new String(result.getRow()));
          Jurisdiction permission = Jurisdiction.getPermission(bean.getPermission());
          if (jurisdictions.contains(permission)) return;
          jurisdictions.add(permission);
        });
    return jurisdictions;
  }

  public List<RoleJurisdicationBean> getAllRoleJurisdictionsByUserName(String roleName) {
    List<RoleJurisdicationBean> roleJurisdicationBeans = new ArrayList<>();
    PrefixFilter filter = new PrefixFilter(roleName.getBytes());
    ResultScanner results = hbaseManager.getScan(TABLENAME, null, filter);
    results.forEach(
        result -> {
          RoleJurisdicationBean bean = new RoleJurisdicationBean(new String(result.getRow()));
          roleJurisdicationBeans.add(bean);
        });
    return roleJurisdicationBeans;
  }

  public boolean updataRoleJurisdictions(String changeRoleName, String[] changePermissionInfos) {
    // 还是先删再添加
    boolean deleteResult = deletePermissionByUsername(changeRoleName);
    if (!deleteResult) return false;
    addRoleJurisdiction(changePermissionInfos, changeRoleName);
    return true;
  }

  private boolean addRoleJurisdiction(String[] changePermissionInfos, String changeRoleName) {
    Arrays.stream(changePermissionInfos)
        .map(s -> new RoleJurisdicationBean(changeRoleName, s).toString())
        .forEach(
            s -> {
              Put put = new Put(s.getBytes());
              // 纯粹就是用来占位置得
              put.addColumn("info".getBytes(), "description".getBytes(), "aa".getBytes());
              hbaseManager.synPut(TABLENAME, put);
            });
    return true;
  }

  private boolean deletePermissionByUsername(String changeRoleName) {
    if (changeRoleName == null) return false;
    List<RoleJurisdicationBean> beans = getAllRoleJurisdictionsByUserName(changeRoleName);
    List<String> rows =
        beans.stream().map(RoleJurisdicationBean::toString).collect(Collectors.toList());
    hbaseManager.delete(TABLENAME, (String[]) rows.toArray());
    return true;
  }
}
