package com.graduation.logic.db.impl;

import com.google.common.base.Preconditions;
import com.graduation.data.bean.UserBean;
import com.graduation.logic.db.HbaseManager;
import com.graduation.util.CommonUtil;
import com.graduation.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Put;
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

  public boolean updateUserInfos(String changeDescription, String changePhone, String username) {
    Put put = new Put(username.getBytes());
    if (changeDescription == null && changePhone == null) return false;
    if (changePhone != null)
      put.addColumn("userinfo".getBytes(), "phone".getBytes(), changePhone.getBytes());
    if (changeDescription != null)
      put.addColumn("userinfo".getBytes(), "description".getBytes(), changeDescription.getBytes());
    hbaseManager.synPut(TABLENAME, put);
    return true;
  }

  public boolean addUserInfos(
      String userName, String addPhonenum, String addUserDescription, String password) {
    // 先初步设定为必须提供全部的信息
    if (StringUtils.isBlank(userName)
        || StringUtils.isBlank(addPhonenum)
        || StringUtils.isBlank(addUserDescription)
        || StringUtils.isBlank(password)) return false;
    Put put = new Put(userName.getBytes());
    put.addColumn(
        "userinfo".getBytes(), "password".getBytes(), MD5Util.getMd5String(password).getBytes());
    put.addColumn("userinfo".getBytes(), "phone".getBytes(), addPhonenum.getBytes());
    put.addColumn("userinfo".getBytes(), "description".getBytes(), addUserDescription.getBytes());
    hbaseManager.synPut(TABLENAME, put);
    return true;
  }
}
