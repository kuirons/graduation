package com.graduation.logic.db.impl;

import com.graduation.data.bean.RoleJurisdicationBean;
import com.graduation.logic.db.HbaseManager;
import com.graduation.security.Jurisdiction;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
}
