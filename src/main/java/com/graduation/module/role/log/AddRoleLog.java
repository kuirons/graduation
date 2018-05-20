package com.graduation.module.role.log;

import com.graduation.log.UserLog;

/**
 * @author WuGYu
 * @date 2018/5/20 20:16
 */
public class AddRoleLog extends UserLog {
  private String roleName;
  private String jurisdication;

  public AddRoleLog(String userName, String roleName, String jurisdication) {
    super(userName);
    this.jurisdication = jurisdication;
    this.roleName = roleName;
  }
}
