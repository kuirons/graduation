package com.graduation.data.bean;

import javax.annotation.Nullable;

/**
 * 处理为单信息等长的形式，非一对一的关系，可能是一对多的关系
 *
 * <p>Created by kuirons on 18-4-7
 */
public class UserRoleBean {
  // 用户名
  private String userName = "";
  // 角色名
  private String roleName = "";

  UserRoleBean(@Nullable String userName, @Nullable String roleName) {
    if (userName != null) this.userName = userName;
    if (roleName != null) this.roleName = roleName;
  }
  // 这个主要用来处理从数据库中查询出来后带有填充字符的情况，其他情况莫用
  UserRoleBean(String result) {
    String r = result.replace("*", "");
    String[] msg = r.split("-");
    this.userName = msg[0];
    this.roleName = msg[1];
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(userName);
    while (result.length() < 20) {
      result.append('*');
    }
    result.append('-').append(roleName);
    while (result.length() < 41) {
      result.append("*");
    }
    return result.toString();
  }
}
