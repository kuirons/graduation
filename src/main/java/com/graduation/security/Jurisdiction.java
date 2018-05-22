package com.graduation.security;

/** Created by kuirons on 18-4-6 */
public enum Jurisdiction {
  g_admin, // 超级用户权限
  g_normal, // 普通用户权限
  g_data, // 数据操作权限
  g_security, // 权限管理权限
  g_log, // 日志管理权限
  ;

  public static Jurisdiction getPermission(String s) {
    switch (s) {
      case "g_admin":
        return Jurisdiction.g_admin;
      case "g_normal":
        return Jurisdiction.g_normal;
      case "g_data":
        return Jurisdiction.g_data;
      case "g_security":
        return Jurisdiction.g_security;
      case "g_log":
        return Jurisdiction.g_log;
      default:
        return null;
    }
  }
}
