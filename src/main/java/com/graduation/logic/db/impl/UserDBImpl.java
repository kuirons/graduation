package com.graduation.logic.db.impl;

import com.google.common.base.Preconditions;
import com.graduation.data.bean.UserBean;
import com.graduation.logic.db.HbaseManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** Created by kuirons on 18-4-7 */
@Repository
public class UserDBImpl {
  private static final String TABLENAME = "graduation_user";
  @Autowired HbaseManager hbaseManager;

  public UserBean getUserByName(String userName) {
    Preconditions.checkNotNull(StringUtils.isBlank(userName), "用户名不能为空哦！！！");
    Result result = hbaseManager.getRow(TABLENAME, userName);
    return new UserBean()
        .setUserName(userName)
        .setPassword(new String(result.getValue("userinfo".getBytes(), "password".getBytes())));
  }
}
