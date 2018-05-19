package com.graduation.data.bean;

public class MessageBean {
  private String fileName;
  private String sendUserName;
  private String content;
  private long time;

  public MessageBean(String result) {
    fileName = result.split("-")[0];
    time = Long.parseLong(result.split("-")[1]);
  }

  public MessageBean() {}

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getContent() {

    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSendUserName() {

    return sendUserName;
  }

  public void setSendUserName(String sendUserName) {
    this.sendUserName = sendUserName;
  }

  public String getFileName() {

    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(fileName);
    buffer.append("-");
    buffer.append(time);
    return buffer.toString();
  }
}
