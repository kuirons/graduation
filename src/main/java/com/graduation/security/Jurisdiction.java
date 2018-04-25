package com.graduation.security;

/** Created by kuirons on 18-4-6 */
public enum Jurisdiction {
  g_insert, // 导入数据的权限
  g_delete, // 删除数据的权限
  g_alter, // 修改数据的权限
  g_select, // 查询数据的权限
  g_admin, // 管理权限的权限
  g_login, // 基本权限，可以访问主页
  ;

  public static Jurisdiction getPermission(String s) {
    switch (s) {
      case "g_insert":
        return Jurisdiction.g_insert;
      case "g_delete":
        return Jurisdiction.g_delete;
      case "g_alter":
        return Jurisdiction.g_alter;
      case "g_select":
        return Jurisdiction.g_select;
      case "g_admin":
        return Jurisdiction.g_admin;
      case "g_login":
        return Jurisdiction.g_login;
      default:
        return null;
    }
  }
}
