package com.graduation.logic.datamanager;

import com.alibaba.fastjson.JSON;
import com.graduation.data.bean.DataBean;
import com.graduation.data.bean.DataGBean;
import com.graduation.data.bean.MessageBean;
import com.graduation.logic.db.impl.DataDBImpl;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.hadoop.fs.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataManager {
  private static int SUBPAN = 144;
  //  private static int DRIVER = 153;
  // 每帧字节数
  private static int FRAME = 1024 * 2;

  @Autowired DataDBImpl dataDB;

  // 先不读取电压数据
  public List<double[][]> formatGData(String fileName) throws IOException {
    List<double[][]> result = new ArrayList<>();
    RandomAccessFile accessFile = new RandomAccessFile(fileName, "r");
    long fileLength = accessFile.length();
    long nowPoint = 0;
    int frame = (int) (fileLength / FRAME);
    double[][] resultX = new double[SUBPAN][frame];
    double[][] resultY = new double[SUBPAN][frame];
    //    double[][] resultV = new double[DRIVER][frame];
    while (nowPoint < fileLength / 2) {
      accessFile.seek(nowPoint * 2);
      for (int i = 0; i < SUBPAN; i++) {
        // 读两个short
        resultX[i][(int) (nowPoint / 1024)] = ((accessFile.readShort() / 256.0) / 256.0);
        resultY[i][(int) (nowPoint / 1024)] = (accessFile.readShort() / 256.0) / 256.0;
      }
      //      accessFile.seek(nowPoint * 2 + 512 * 2);
      //      for (int i = 0; i < DRIVER; i++) {
      //        resultV[i][(int) (nowPoint / 1024)] = (accessFile.readShort());
      //      }
      nowPoint += 1024;
    }
    result.add(resultX);
    result.add(resultY);
    //    result.add(resultV);
    accessFile.close();
    // 读完数据就删除
    FileUtil.fullyDelete(new File(fileName));
    return result;
  }

  public void saveDataToDB(
      List<double[][]> data, String userName, String description, String fileName) {
    long time = System.currentTimeMillis();
    if (data.size() > 2 || data.size() <= 0) return;
    // 这里将数据按不同子孔镜分开，然后存储。
    for (int i = 0; i < data.get(0).length; i++) {
      List<Double> sx = getStatisticsData(data.get(0)[i]);
      List<Double> sy = getStatisticsData(data.get(1)[i]);
      DataGBean dataGBean = new DataGBean();
      dataGBean.setUserName(userName);
      dataGBean.setSequence(i);
      dataGBean.setTime(time);
      dataGBean.setJsonX(JSON.toJSONString(data.get(0)[i]));
      dataGBean.setJsonY(JSON.toJSONString(data.get(1)[i]));
      dataGBean.setJsonStatisticsX(JSON.toJSONString(sx));
      dataGBean.setJsonStatisticsY(JSON.toJSONString(sy));
      dataGBean.setFileName(fileName);
      dataDB.saveDataAll(dataGBean);
    }
    // 这里存储数据的整体情况
    DataBean dataBean = new DataBean();
    dataBean.setFileName(fileName);
    dataBean.setUserName(userName);
    dataBean.setTime(time);
    dataBean.setDescription(description);
    // 这个操作比较耗费时间
    dataBean.setJsonStatisticsX(JSON.toJSONString(getStatisticsData(data.get(0))));
    dataBean.setJsonStatisticsY(JSON.toJSONString(getStatisticsData(data.get(1))));
    dataDB.saveData(dataBean);
  }

  private List<Double> getStatisticsData(double[] data) {
    DescriptiveStatistics stats = new DescriptiveStatistics();
    Arrays.stream(data).forEach(e -> stats.addValue(e));
    List<Double> result = new ArrayList<>();
    // 最大值
    result.add(stats.getMax());
    // 最小值
    result.add(stats.getMin());
    // 算术平均值
    result.add(stats.getMean());
    // 标准方差
    result.add(stats.getStandardDeviation());
    return result;
  }

  private List<Double> getStatisticsData(double[][] data) {
    DescriptiveStatistics stats = new DescriptiveStatistics();
    Arrays.stream(data).forEach(e -> Arrays.stream(e).forEach(e1 -> stats.addValue(e1)));
    List<Double> result = new ArrayList<>();
    // 最大值
    result.add(stats.getMax());
    // 最小值
    result.add(stats.getMin());
    // 算术平均值
    result.add(stats.getMean());
    // 标准方差
    result.add(stats.getStandardDeviation());
    return result;
  }

  public List<DataBean> getData() {
    return dataDB.scanData();
  }

  public List<DataGBean> getSingleData(String fileName) {
    return dataDB.getSingleData(fileName);
  }

  public DataBean getSimpleData(String fileName) {
    return dataDB.getSimpleData(fileName);
  }

  public boolean saveContent(String content, Object userName,String fileName) {
    MessageBean messageBean = new MessageBean();
    messageBean.setFileName(fileName);
    messageBean.setContent(content);
    messageBean.setSendUserName((String) userName);
    messageBean.setTime(System.currentTimeMillis());
    return dataDB.saveContent(messageBean);
  }

  public List<MessageBean> getAllComment(String fileName) {
    return dataDB.getContentByFileName(fileName);
  }
}
