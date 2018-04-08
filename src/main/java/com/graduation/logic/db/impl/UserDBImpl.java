package com.graduation.logic.db.impl;

import com.graduation.data.bean.UserBean;
import com.graduation.logic.db.HbaseManager;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** Created by kuirons on 18-4-7 */
@Repository
public class UserDBImpl {
  private static final String TABLENAME = "graduation_user";
  @Autowired HbaseManager hbaseManager;

  public UserBean getUserByName(String userName) {
    Result result = hbaseManager.getRow(TABLENAME, userName);
    //      return new
    // UserBean().setUserName(result.getColumnCells("userinfo".getBytes(),"password".getBytes()).)
    return null;
  }
}
