package com.graduation.logic.initialize;

import com.graduation.logic.db.HbaseManager;
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

  private static final String TABLE_NAME = "graduation_userinfo";
  private static final String DEFAULT_USER_NAME = "admin";
  private static final String DEFAULT_USER_PASSWOAR = "admin";
  private static final String DEFAULT_USER_AUTHORITY = "admin";
  private static final String DEFAULT_SALT = "graduation";

  @Autowired HbaseManager hbaseManager;

  @Override
  public void afterPropertiesSet() throws Exception {
    LOGGER.info("开始检查权限表");
    // 这里检查数据库表是否完备，如果不完备，创建默认的表并添加默认的用户
    if (!hbaseManager.existTable(TABLE_NAME)) {
      LOGGER.info("默认表不存在，开始创建默认权限表");
      hbaseManager.createTable(TABLE_NAME, new String[] {"userinfo"});
      Put put = new Put(DEFAULT_USER_NAME.getBytes());
      // password
      put.addColumn(
          "userinfo".getBytes(),
          "password".getBytes(),
          new Md5PasswordEncoder().encodePassword(DEFAULT_USER_PASSWOAR, DEFAULT_SALT).getBytes());
      // authority
      put.addColumn(
          "userinfo".getBytes(), "authority".getBytes(), DEFAULT_USER_AUTHORITY.getBytes());
      hbaseManager.synPut(TABLE_NAME, put);
      LOGGER.info("表创建完毕");
      return;
    }
    LOGGER.info("权限表ok！！！");
  }
}
