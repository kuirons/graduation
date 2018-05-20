package com.graduation.module.data.log;

import com.graduation.log.UserLog;

/**
 * @author WuGYu
 * @date 2018/5/20 17:35
 */
public class CheckDataLog extends UserLog {
  private String fileName;

  public CheckDataLog(String userName, String fileName) {
    super(userName);
    this.fileName = fileName;
  }
}
