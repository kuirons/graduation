package com.graduation.module.role.log;

import com.graduation.log.UserLog;

/**
 * @author WuGYu
 * @date 2018/5/20 20:19
 */
public class ChangeRoleLog extends UserLog {
  private String roleName;
  private String jurisdiction;

  public ChangeRoleLog(String userName, String roleName, String jurisdiction) {
    super(userName);
    this.jurisdiction = jurisdiction;
    this.roleName = roleName;
  }
}
