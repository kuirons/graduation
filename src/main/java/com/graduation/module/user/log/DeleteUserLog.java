package com.graduation.module.user.log;

import com.graduation.log.UserLog;

/**
 * @author WuGYu
 * @date 2018/5/20 20:08
 */
public class DeleteUserLog extends UserLog {
  private String deleteUserName;

  public DeleteUserLog(String userName, String deleteUserName) {
    super(userName);
    this.deleteUserName = deleteUserName;
  }
}
