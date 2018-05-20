package com.graduation.module.role.log;

import com.graduation.log.UserLog;

/**
 * @author WuGYu
 * @date 2018/5/20 20:26
 */
public class DeleteRoleLog extends UserLog {
  private String deleteUserName;

  public DeleteRoleLog(String userName, String deleteUserName) {
    super(userName);
    this.deleteUserName = deleteUserName;
  }
}
