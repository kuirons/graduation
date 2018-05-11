package com.graduation.data.bean;

/** 每个子孔镜的信息 */
public class DataGBean {
  private String fileName;
  private String userName;
  private String jsonX;
  private String jsonY;
  private String jsonStatisticsX;
  private String jsonStatisticsY;
  private long time;
  private int sequence;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getJsonStatisticsY() {
    return jsonStatisticsY;
  }

  public void setJsonStatisticsY(String jsonStatisticsY) {
    this.jsonStatisticsY = jsonStatisticsY;
  }

  public String getJsonStatisticsX() {

    return jsonStatisticsX;
  }

  public void setJsonStatisticsX(String jsonStatisticsX) {
    this.jsonStatisticsX = jsonStatisticsX;
  }

  public int getSequence() {
    return sequence;
  }

  public void setSequence(int sequence) {
    this.sequence = sequence;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getJsonY() {

    return jsonY;
  }

  public void setJsonY(String jsonY) {
    this.jsonY = jsonY;
  }

  public String getJsonX() {

    return jsonX;
  }

  public void setJsonX(String jsonX) {
    this.jsonX = jsonX;
  }

  public String getUserName() {

    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public DataGBean() {}
  // 这个主要用来处理从数据库中查询出来后带有填充字符的情况，其他情况莫用
  public DataGBean(String row) {
    String r = row.replace("*", "");
    String[] msg = r.split("-");
    this.userName = msg[0];
    this.fileName = msg[1];
    this.sequence = Integer.parseInt(msg[2]);
    this.time = Long.parseLong(msg[3]);
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    StringBuilder num = new StringBuilder();
    for (int i = 0; i < 4 - String.valueOf(sequence).length(); i++) {
      num.append('*');
    }
    num.append(sequence);
    result.append(userName);
    while (result.length() < 20) {
      result.append('*');
    }
    result.append('-').append(fileName);
    while (result.length() < 60) {
      result.append('*');
    }

    result.append('-').append(num.toString()).append('-').append(time);
    return result.toString();
  }
}
