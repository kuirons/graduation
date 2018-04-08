package com.graduation.data.bean;

/**
 * @author WuGYu
 * @date 2018/4/8 14:14
 */
public class RoleJurisdicationBean {
  private String roleName;
  private String permission;

  public RoleJurisdicationBean(String roleName, String permission) {
    this.roleName = roleName;
    this.permission = permission;
  }

  public RoleJurisdicationBean(String result) {
    String r = result.replace("*", "");
    String[] msg = r.split("-");
    this.roleName = msg[0];
    this.permission = msg[1];
  }

  public RoleJurisdicationBean(RoleBean roleBean, JurisdicationBean jurisdicationBean) {
    this.roleName = roleBean.getRole();
    this.permission = jurisdicationBean.getPermission();
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public String getRoleName() {

    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(roleName);
    while (result.length() < 20) {
      result.append('*');
    }
    result.append('-').append(permission);
    while (result.length() < 41) {
      result.append("*");
    }
    return result.toString();
  }
}
