package com.graduation.log;

/**
 * @author WuGYu
 * @date 2018/5/20 17:14
 */
public class UserLog extends AbstractLog {
  private String userName;

  public UserLog(String userName) {
    this.userName = userName;
  }
}
