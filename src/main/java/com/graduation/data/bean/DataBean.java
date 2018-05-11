package com.graduation.data.bean;

/** 这个是一个文件总数据的相关信息 */
public class DataBean {
  private String fileName;
  private String userName;
  private long time;
  private String jsonStatisticsX;
  private String jsonStatisticsY;
  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public long getTime() {

    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getUserName() {

    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getFileName() {

    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  // 这个主要用来处理从数据库中查询出来后带有填充字符的情况，其他情况莫用
  public DataBean(String row) {
    String r = row.replace("*", "");
    String[] msg = r.split("-");
    this.userName = msg[0];
    this.fileName = msg[1];
    this.time = Long.parseLong(msg[2]);
  }

  public DataBean() {}

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(userName);
    while (result.length() < 20) {
      result.append('*');
    }
    result.append('-').append(fileName);
    while (result.length() < 60) {
      result.append('*');
    }
    result.append('-').append(time);
    return result.toString();
  }
}
