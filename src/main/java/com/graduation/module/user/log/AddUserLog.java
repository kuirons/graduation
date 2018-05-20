package com.graduation.module.user.log;

import com.graduation.log.UserLog;

/**
 * @author WuGYu
 * @date 2018/5/20 17:43
 */
public class AddUserLog extends UserLog {
  private String newUserName;
  private String newRole;
  private String phoneNum;

  public AddUserLog(String userName, String newUserName, String newRole, String phoneNum) {
    super(userName);
    this.newUserName = newUserName;
    this.newRole = newRole;
    this.phoneNum = phoneNum;
  }
}
