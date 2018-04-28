package com.graduation.logic.db.impl;

import com.google.common.base.Preconditions;
import com.graduation.data.bean.UserBean;
import com.graduation.logic.db.HbaseManager;
import com.graduation.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/** Created by kuirons on 18-4-7 */
@Repository
public class UserDBImpl {
  private static final String TABLENAME = "graduation_user";
  @Autowired HbaseManager hbaseManager;

  public UserBean getUserByName(String userName) {
    Preconditions.checkNotNull(StringUtils.isBlank(userName), "用户名不能为空哦！！！");
    Result result = hbaseManager.getRow(TABLENAME, userName);
    if (result.isEmpty() == true) return null;
    return new UserBean()
        .setUserName(userName)
        .setPassword(
            CommonUtil.bytesToString(
                result.getValue("userinfo".getBytes(), "password".getBytes())));
  }

  public List<UserBean> getAllUser() {
    List<UserBean> userBeans = new ArrayList<>();
    List<Result> results = hbaseManager.scanAllTable(TABLENAME);
    if (results == null) return null;
    results.forEach(
        result ->
            userBeans.add(
                new UserBean()
                    .setUserName(CommonUtil.bytesToString(result.getRow()))
                    .setDescription(
                        CommonUtil.bytesToString(
                            result.getValue("userinfo".getBytes(), "description".getBytes())))
                    .setPhone(
                        CommonUtil.bytesToString(
                            result.getValue("userinfo".getBytes(), "phone".getBytes())))));
    return userBeans;
  }
}
