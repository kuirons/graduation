package com.graduation.logic.db.impl;

import com.graduation.data.bean.DataBean;
import com.graduation.data.bean.DataGBean;
import com.graduation.logic.db.HbaseManager;
import com.graduation.util.CommonUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Repository
public class DataDBImpl {
  // 这是用来存放详细信息的表
  private static final String TABLE_NAME_ALL = "graduation_data_all";
  // 这是用来存放粗略信息的表
  private static final String TABLE_NAME = "graduation_data";
  @Autowired HbaseManager hbaseManager;

  public void saveDataAll(DataGBean dataGBean) {
    Put put = new Put(dataGBean.toString().getBytes());
    put.addColumn("datainfo".getBytes(), "x".getBytes(), dataGBean.getJsonX().getBytes());
    put.addColumn("datainfo".getBytes(), "y".getBytes(), dataGBean.getJsonY().getBytes());
    put.addColumn(
        "datainfo".getBytes(), "statisticsx".getBytes(), dataGBean.getJsonStatisticsX().getBytes());
    put.addColumn(
        "datainfo".getBytes(), "statisticsy".getBytes(), dataGBean.getJsonStatisticsY().getBytes());
    hbaseManager.synPut(TABLE_NAME_ALL, put);
  }

  public DataGBean getDataAll(String row) {
    Result result = hbaseManager.getRow(TABLE_NAME_ALL, row);
    DataGBean dataGBean = new DataGBean(CommonUtil.bytesToString(result.getRow()));
    dataGBean.setJsonX(
        CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "x".getBytes())));
    dataGBean.setJsonY(
        CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "y".getBytes())));
    dataGBean.setJsonStatisticsX(
        CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "statisticsx".getBytes())));
    dataGBean.setJsonStatisticsY(
        CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "statisticsy".getBytes())));
    return dataGBean;
  }

  public void saveData(DataBean dataBean) {
    Put put = new Put(dataBean.toString().getBytes());
    put.addColumn(
        "datainfo".getBytes(), "description".getBytes(), dataBean.getDescription().getBytes());
    put.addColumn(
        "datainfo".getBytes(), "statisticsx".getBytes(), dataBean.getJsonStatisticsX().getBytes());
    put.addColumn(
        "datainfo".getBytes(), "statisticsy".getBytes(), dataBean.getJsonStatisticsY().getBytes());
    hbaseManager.synPut(TABLE_NAME, put);
  }

  public DataBean getData(String row) {
    Result result = hbaseManager.getRow(TABLE_NAME, row);
    DataBean dataBean = new DataBean(CommonUtil.bytesToString(result.getRow()));
    dataBean.setDescription(
        CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "description".getBytes())));
    dataBean.setJsonStatisticsX(
        CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "statisticsx".getBytes())));
    dataBean.setJsonStatisticsY(
        CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "statisticsy".getBytes())));
    return dataBean;
  }

  public List<DataBean> scanData() {
    List<Result> results = hbaseManager.scanAllTable(TABLE_NAME);
    List<DataBean> dataBeans = new ArrayList<>();
    results.forEach(
        result -> {
          DataBean dataBean = new DataBean(CommonUtil.bytesToString(result.getRow()));
          dataBean.setDescription(
              CommonUtil.bytesToString(
                  result.getValue("datainfo".getBytes(), "description".getBytes())));
          dataBean.setJsonStatisticsX(
              CommonUtil.bytesToString(
                  result.getValue("datainfo".getBytes(), "statisticsx".getBytes())));
          dataBean.setJsonStatisticsY(
              CommonUtil.bytesToString(
                  result.getValue("datainfo".getBytes(), "statisticsy".getBytes())));
          dataBeans.add(dataBean);
        });
    return dataBeans;
  }

  public List<DataGBean> getSingleData(String fileName) {
    String uName = fileName.split("-")[0];
    String fName = fileName.split("-")[1];
    StringBuilder builder = new StringBuilder();
    builder.append(uName);
    while (builder.length() < 20) builder.append('*');
    builder.append('-').append(fName);
    while (builder.length() < 60) builder.append('*');
    builder.append('-').append("!!!!").append('-').append("!!!!!!!!!!!!!");
    // 这里构建两个用来限制范围的行键
    String startRow = builder.toString();
    builder = new StringBuilder();
    builder.append(uName);
    while (builder.length() < 20) builder.append('*');
    builder.append('-').append(fName);
    while (builder.length() < 60) builder.append('*');
    builder.append('-').append("䶵䶵").append('-').append("aaaaaaaaaaaaa");
    String endRow = builder.toString();
    HashMap<String, List<String>> param = new HashMap<>();
    List<String> start = new ArrayList<>();
    start.add(startRow);
    param.put("startRow", start);
    List<String> end = new ArrayList<>();
    end.add(endRow);
    param.put("stopRow", end);
    List<Result> results = hbaseManager.getRowsByLimit(TABLE_NAME_ALL, param, null);
    List<DataGBean> dataGBeans = new ArrayList<>();
    results.forEach(
        result -> {
          DataGBean dataGBean = new DataGBean(CommonUtil.bytesToString(result.getRow()));
          dataGBean.setJsonX(
              CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "x".getBytes())));
          dataGBean.setJsonY(
              CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "y".getBytes())));
          dataGBean.setJsonStatisticsX(
              CommonUtil.bytesToString(
                  result.getValue("datainfo".getBytes(), "statisticsx".getBytes())));
          dataGBean.setJsonStatisticsY(
              CommonUtil.bytesToString(
                  result.getValue("datainfo".getBytes(), "statisticsy".getBytes())));
          dataGBeans.add(dataGBean);
        });
    return dataGBeans;
  }

  public DataBean getSimpleData(String fileName) {
    String uName = fileName.split("-")[0];
    String fName = fileName.split("-")[1];
    StringBuilder builder = new StringBuilder();
    builder.append(uName);
    while (builder.length() < 20) builder.append('*');
    builder.append('-').append(fName);
    while (builder.length() < 60) builder.append('*');
    PrefixFilter filter = new PrefixFilter(builder.toString().getBytes());
    ResultScanner scanner = hbaseManager.getScan(TABLE_NAME, null, filter);
    Iterator<Result> iterator = scanner.iterator();
    Result result = iterator.next();
    DataBean dataBean = new DataBean(CommonUtil.bytesToString(result.getRow()));
    dataBean.setDescription(
        CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "description".getBytes())));
    dataBean.setJsonStatisticsX(
        CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "statisticsx".getBytes())));
    dataBean.setJsonStatisticsY(
        CommonUtil.bytesToString(result.getValue("datainfo".getBytes(), "statisticsy".getBytes())));
    return dataBean;
  }
}
