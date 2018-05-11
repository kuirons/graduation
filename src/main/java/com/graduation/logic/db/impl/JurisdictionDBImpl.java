package com.graduation.logic.db.impl;

import com.graduation.data.bean.JurisdictionBean;
import com.graduation.logic.db.HbaseManager;
import com.graduation.util.CommonUtil;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/** Created by kuirons on 18-4-7 */
@Repository
public class JurisdictionDBImpl {
  private static final String TABLE_NAME = "graduation_jurisdiction";
  @Autowired HbaseManager hbaseManager;

  public List<JurisdictionBean> getAllPermissions() {
    List<Result> results = hbaseManager.scanAllTable(TABLE_NAME);
    List<JurisdictionBean> jurisdictionBeans = new ArrayList<>();
    results.forEach(
        result -> {
          JurisdictionBean jurisdictionBean = new JurisdictionBean();
          jurisdictionBean.setDescription(
              CommonUtil.bytesToString(
                  result.getValue("jurisdictioninfo".getBytes(), "description".getBytes())));
          jurisdictionBean.setPermission(CommonUtil.bytesToString(result.getRow()));
          jurisdictionBeans.add(jurisdictionBean);
        });
    return jurisdictionBeans;
  }
}
