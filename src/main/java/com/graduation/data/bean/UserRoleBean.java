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

  public UserRoleBean(@Nullable String userName, @Nullable String roleName) {
    if (userName != null) this.userName = userName;
    if (roleName != null) this.roleName = roleName;
  }
  // 这个主要用来处理从数据库中查询出来后带有填充字符的情况，其他情况莫用
  public UserRoleBean(String result) {
    String r = result.replace("*", "");
    String[] msg = r.split("-");
    this.userName = msg[0];
    this.roleName = msg[1];
  }

  public UserRoleBean(UserBean userBean, RoleBean roleBean) {
    this.userName = userBean.getUserName();
    this.roleName = roleBean.getRole();
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getUserName() {

    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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
