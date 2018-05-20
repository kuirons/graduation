package com.graduation.module.data.log;

import com.graduation.log.UserLog;

/**
 * @author WuGYu
 * @date 2018/5/20 17:18
 */
public class DataUploadLog extends UserLog {
  private String fileName;

  public DataUploadLog(String userName, String fileName) {
    super(userName);
    this.fileName = fileName;
  }
}
