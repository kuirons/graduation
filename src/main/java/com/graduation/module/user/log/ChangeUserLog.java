package com.graduation.module.user.log;

import com.graduation.log.UserLog;

/**
 * @author WuGYu
 * @date 2018/5/20 17:50
 */
public class ChangeUserLog extends UserLog {
  private String changeUserName;
  private String changeRole;
  private String changeUserPhone;

  public ChangeUserLog(
      String userName, String changeUserName, String changeRole, String changeUserPhone) {
    super(userName);
    this.changeUserName = changeUserName;
    this.changeRole = changeRole;
    this.changeUserPhone = changeUserPhone;
  }
}
