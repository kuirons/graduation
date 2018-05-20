package com.graduation.logic.initialize;

import com.graduation.data.bean.RoleJurisdicationBean;
import com.graduation.data.bean.UserRoleBean;
import com.graduation.logic.db.HbaseManager;
import com.graduation.util.MD5Util;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/** Created by kuirons on 18-4-6 */
@Service
public class AppInitialize implements InitializingBean {
  private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HbaseManager.class);

  @Autowired HbaseManager hbaseManager;

  // todo 表关系维护，同时使删除更为平滑
  // 这里主要保证程序运行需要的基本表存在
  @Override
  public void afterPropertiesSet() throws Exception {
    LOGGER.info("开始检查表是否存在");
    // 这里检查数据库表是否完备，如果不完备，创建默认的表并添加默认的用户
    LOGGER.info("开始检查用户表");
    checkTable("graduation_user");
    LOGGER.info("用户表OK！");
    LOGGER.info("开始检查角色表");
    checkTable("graduation_role");
    LOGGER.info("角色表OK！");
    LOGGER.info("开始检查权限表");
    checkTable("graduation_jurisdiction");
    LOGGER.info("权限表OK！");
    LOGGER.info("开始检查用户-角色表");
    checkTable("graduation_user_role");
    LOGGER.info("用户-角色表OK！");
    LOGGER.info("开始检查角色-权限表");
    checkTable("graduation_role_jurisdiction");
    LOGGER.info("角色-权限表OK！");
    checkTable("graduation_data");
    LOGGER.info("通用数据表OK！");
    checkTable("graduation_data_all");
    LOGGER.info("数据表OK！");
    checkTable("graduation_comment");
    LOGGER.info("评论表OK！");
  }

  // 表的关系自己维护
  private void checkTable(String tableName) {
    String msg;
    Put put;
    String[] families;
    List<Put> puts = new ArrayList<>();
    switch (tableName) {
      case "graduation_user":
        msg = "用户";
        put = new Put("admin".getBytes());
        put.addColumn(
            "userinfo".getBytes(), "password".getBytes(), MD5Util.getMd5String("admin").getBytes());
        put.addColumn("userinfo".getBytes(), "phone".getBytes(), "13548251111".getBytes());
        put.addColumn(
            "userinfo".getBytes(), "description".getBytes(), "这是超级管理员，可以为所欲为，而且你不能删除他".getBytes());
        puts.add(put);
        families = new String[] {"userinfo"};
        break;
      case "graduation_role":
        msg = "角色";
        put = new Put("ADMIN".getBytes());
        put.addColumn(
            "roleinfo".getBytes(),
            "description".getBytes(),
            "这是超级角色，可以为所欲为，你应该把所有的权限都给他，或者，把最高的权限给他".getBytes());
        puts.add(put);
        families = new String[] {"roleinfo"};
        break;
      case "graduation_jurisdiction":
        msg = "权限";
        put = new Put("g_admin".getBytes());
        put.addColumn("jurisdictioninfo".getBytes(), "description".getBytes(), "超级用户权限".getBytes());
        puts.add(put);
        put = new Put("g_test".getBytes());
        put.addColumn("jurisdictioninfo".getBytes(), "description".getBytes(), "测试权限".getBytes());
        puts.add(put);
        families = new String[] {"jurisdictioninfo"};
        break;
      case "graduation_user_role":
        msg = "用户-角色";
        // 前缀已经处理过了
        new UserRoleBean("admin", "ADMIN");
        put = new Put(new UserRoleBean("admin", "ADMIN").toString().getBytes());
        put.addColumn("info".getBytes(), "description".getBytes(), "aa".getBytes());
        puts.add(put);
        families = new String[] {"info"};
        break;
      case "graduation_role_jurisdiction":
        msg = "角色-权限";
        put = new Put(new RoleJurisdicationBean("ADMIN", "g_admin").toString().getBytes());
        put.addColumn("info".getBytes(), "description".getBytes(), "aa".getBytes());
        puts.add(put);
        families = new String[] {"info"};
        break;
      case "graduation_data_all":
        msg = "详细数据";
        families = new String[] {"datainfo"};
        break;
      case "graduation_data":
        msg = "通用数据";
        families = new String[] {"datainfo"};
        break;
      case "graduation_comment":
        msg = "评论";
        families = new String[] {"commentinfo"};
        break;
      default:
        return;
    }
    try {
      checkTable(tableName, msg, families, puts);
    } catch (Exception e) {
      LOGGER.error("启动失败");
      System.exit(-1);
    }
  }

  private void checkTable(
      String tableName, String msg, @Nullable String[] families, @Nullable List<Put> puts)
      throws Exception {
    if (!hbaseManager.existTable(tableName)) {
      LOGGER.info(msg + "表不存在，开始创建");
      hbaseManager.createTable(tableName, families);
      if ((puts != null) && (puts.size() > 0)) hbaseManager.synPuts(tableName, puts);
      LOGGER.info(msg + "表创建完毕");
    }
  }
}
