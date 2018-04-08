package com.graduation.logic.initialize;

import com.graduation.logic.db.HbaseManager;
import com.graduation.security.Jurisdiction;
import com.graduation.util.MD5Util;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

/** Created by kuirons on 18-4-6 */
@Service
public class AppInitialize implements InitializingBean {
  private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HbaseManager.class);

  @Autowired HbaseManager hbaseManager;

  // 这里主要保证程序运行需要的基本表存在
  @Override
  public void afterPropertiesSet() throws Exception {
    LOGGER.info("开始检查表是否存在");
    // 这里检查数据库表是否完备，如果不完备，创建默认的表并添加默认的用户
    checkTable("graduation_user");
    checkTable("graduation_role");
    checkTable("graduation_jurisdiction");
    checkTable("graduation_user_role");
    checkTable("graduation_role_jurisdiction");
  }

  // 表的关系自己维护
  private void checkTable(String tableName) {
    String msg;
    Put put;
    String[] families;
    switch (tableName) {
      case "graduation_user":
        msg = "用户";
        put = new Put("admin".getBytes());
        put.addColumn(
            "userinfo".getBytes(), "password".getBytes(), MD5Util.getMd5String("admin").getBytes());
        families = new String[] {"userinfo"};
        break;
      case "graduation_role":
        msg = "角色";
        put = new Put("ROLE_ADMIN".getBytes());
        put.addColumn("roleinfo".getBytes(), "description".getBytes(), "管理员".getBytes());
        families = new String[] {"roleinfo"};
        break;
      case "graduation_jurisdiction":
        msg = "权限";
        put = new Put("admin".getBytes());
        put.addColumn("jurisdictioninfo".getBytes(), "description".getBytes(), "超级用户权限".getBytes());
        families = new String[] {"jurisdictioninfo"};
        break;
      case "graduation_user_role":
        msg = "用户-角色";
        put = new Put("admin".getBytes());
        put.addColumn("userroleinfo".getBytes(), "role".getBytes(), "ROLE_ADMIN".getBytes());
        families = new String[] {"userroleinfo"};
        break;
      case "graduation_role_jurisdiction":
        msg = "角色-权限";
        put = new Put("ROLE_ADMIN".getBytes());
        put.addColumn(
            "rolejurisdictioninfo".getBytes(),
            "jurisdiction".getBytes(),
            Jurisdiction.g_admin.toString().getBytes());
        families = new String[] {"rolejurisdictioninfo"};
        break;
      default:
        return;
    }
    try {
      checkTable(tableName, msg, families, put);
    } catch (Exception e) {
      LOGGER.error("启动失败");
      System.exit(-1);
    }
  }

  private void checkTable(String tableName, String msg, String[] families, Put put)
      throws Exception {
    if (!hbaseManager.existTable(tableName)) {
      LOGGER.info(msg + "表不存在，开始创建");
      hbaseManager.createTable(tableName, families);
      hbaseManager.synPut(tableName, put);
      LOGGER.info(msg + "表创建完毕");
    }
  }
}
