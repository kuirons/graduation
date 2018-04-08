package com.graduation.logic.db.impl;

import com.graduation.data.bean.UserRoleBean;
import com.graduation.logic.db.HbaseManager;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WuGYu
 * @date 2018/4/8 14:42
 */
@Repository
public class UserRoleDBImpl {
  private static final String TABLENAME = "graduation_user_role";
  @Autowired HbaseManager hbaseManager;

  public List<UserRoleBean> getUserRoleByUserName(String userName) {
    List<UserRoleBean> userRoleBeans = new ArrayList<>();
    // 先构造一个前缀匹配器
    PrefixFilter filter = new PrefixFilter(userName.getBytes());
    ResultScanner scanner = hbaseManager.getScan(TABLENAME, null, filter);
    scanner.forEach(result -> userRoleBeans.add(new UserRoleBean(new String(result.getRow()))));
    return userRoleBeans;
  }
}
