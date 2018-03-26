package com.graduation.logic.db;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author WuGYu
 * @date 2018/3/24 14:03
 */
@Repository
public class HbaseManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(HbaseManager.class);

  private static Configuration configuration;
  private static Connection connection;

  // 初始化hbase的region
  private static final String[] SPLIT_KEYS =
      new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E"};

  /** 初始化连接 */
  static {
    try {
      if (configuration == null) {
        configuration = HBaseConfiguration.create();
        try {
          if (connection == null) {
            connection = ConnectionFactory.createConnection();
          }
        } catch (IOException e) {
          LOGGER.error("获取Hbase连接失败", e);
        }
      }
    } catch (Exception e) {
      LOGGER.error("加载Hbase配置文件失败", e);
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取hbase连接，同一时间只允许一个请求获取到连接
   *
   * @return
   */
  public synchronized Connection getConnection() {
    try {
      if (connection == null || connection.isClosed()) {
        connection = ConnectionFactory.createConnection();
      }
    } catch (IOException e) {
      LOGGER.error("获取Hbase连接失败", e);
    }
    return connection;
  }

  /** 关闭连接 */
  public void closeConnection() {
    if (connection != null) {
      try {
        connection.close();
      } catch (IOException e) {
        LOGGER.error("关闭Hbase连接失败", e);
      }
    }
  }

  private void tableAddFamilies(HTableDescriptor tableDescriptor, String cfs) {
    HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cfs);
    // 设置压缩方式
    hColumnDescriptor.setCompactionCompressionType(Compression.Algorithm.SNAPPY);
    // 只保存一个版本，不保存历史值，哎呀历史值会通过其他方式存的
    hColumnDescriptor.setMaxVersions(1);
    tableDescriptor.addFamily(hColumnDescriptor);
  }

  /**
   * 提供分区域key
   *
   * @param tableName
   * @param columFamilies
   * @param splitKeys
   * @throws Exception
   */
  public void createTable(String tableName, String[] columFamilies, @Nullable byte[][] splitKeys)
      throws Exception {
    Connection connection = getConnection();
    HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
    try {
      if (admin.tableExists(tableName)) {
        LOGGER.warn("表:{}已存在", tableName);
        return;
      }
      HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
      for (String cfs : columFamilies) {
        tableAddFamilies(tableDescriptor, cfs);
      }
      if (splitKeys == null) admin.createTable(tableDescriptor);
      else admin.createTable(tableDescriptor, splitKeys);
      LOGGER.info("Table:{}已建立", tableName);
    } finally {
      admin.close();
    }
  }

  /**
   * @param tableName
   * @param columFamilies
   * @throws Exception
   */
  public void createTable(String tableName, String[] columFamilies) throws Exception {
    createTable(tableName, columFamilies, null);
  }

  /**
   * 使用默认分区key对表进行分区
   *
   * @param tableName
   * @param columFamilies
   * @param preBuildRegion
   * @throws Exception
   */
  public void createTable(String tableName, String[] columFamilies, boolean preBuildRegion)
      throws Exception {
    byte[][] splitKey = null;
    if (preBuildRegion) {
      splitKey = new byte[14][];
      for (int i = 0; i < 14; i++) {
        splitKey[i] = Bytes.toBytes(SPLIT_KEYS[i]);
      }
    }
    createTable(tableName, columFamilies, splitKey);
  }

  /**
   * 删除表
   *
   * @param tableName
   * @throws IOException
   */
  public void deleteTable(String tableName) throws IOException {
    Connection connection = getConnection();
    HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
    try {
      if (!admin.tableExists(tableName)) {
        LOGGER.error("表:{}删除失败", tableName);
        return;
      }
      admin.disableTable(tableName);
      admin.deleteTable(tableName);
      LOGGER.info("表:{}删除成功", tableName);
    } finally {
      admin.close();
    }
  }

  /**
   * 获取表
   *
   * @param tableName
   * @return
   */
  public Table getTable(String tableName) {
    try {
      Table table = getConnection().getTable(TableName.valueOf(tableName));
      return table;
    } catch (IOException e) {
      LOGGER.error("获取表:{}失败", tableName);
    }
    return null;
  }

  /**
   * 获取表中经过滤链后结果中的第一行数据
   *
   * @param tableName
   * @param filterList
   * @return
   */
  public Result selectFirstResultRow(String tableName, FilterList filterList) {
    if (StringUtils.isBlank(tableName)) return null;
    Table table = null;
    try {
      table = getTable(tableName);
      Scan scan = new Scan();
      if (filterList != null) {
        scan.setFilter(filterList);
      }
      ResultScanner scanner = table.getScanner(scan);
      Iterator<Result> iterator = scanner.iterator();
      if (iterator.hasNext()) {
        Result rs = iterator.next();
        scanner.close();
        return rs;
      } else scanner.close();
    } catch (IOException e) {
      LOGGER.error("获取表:{}第一行记录失败", tableName, e);
    } finally {
      try {
        table.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
  }

  public long asynPut(String tableName, List<Put> puts) throws Exception {
    // 当前系统时间
    long currentTime = System.currentTimeMillis();
    Connection connection = getConnection();
    final BufferedMutator.ExceptionListener listener =
        new BufferedMutator.ExceptionListener() {
          @Override
          public void onException(
              RetriesExhaustedWithDetailsException e, BufferedMutator bufferedMutator)
              throws RetriesExhaustedWithDetailsException {
            for (int i = 0; i < e.getNumExceptions(); i++) {
              LOGGER.error("异步添加数据:{}失败", e.getRow(i));
            }
          }
        };
    BufferedMutatorParams params =
        new BufferedMutatorParams(TableName.valueOf(tableName)).listener(listener);
    params.writeBufferSize(5 * 1024 * 1024);
    final BufferedMutator mutator = connection.getBufferedMutator(params);
    return 0;
  }
}
