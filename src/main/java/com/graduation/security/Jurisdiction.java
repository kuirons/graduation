package com.graduation.security;

/**
 * Created by kuirons on 18-4-6
 */
public enum Jurisdiction {
    g_insert, // 导入数据的权限
    g_delete, // 删除数据的权限
    g_alter, // 修改数据的权限
    g_select, // 查询数据的权限
    g_admin, // 管理权限的权限
    g_db, // 操作数据库的权限
    ;
}
