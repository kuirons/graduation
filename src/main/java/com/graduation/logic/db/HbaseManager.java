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
<<<<<<< HEAD
import java.util.*;
=======
import java.util.Iterator;
import java.util.List;
>>>>>>> parent of ab5639f... hbase1

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
<<<<<<< HEAD
    try {
      mutator.mutate(puts);
      mutator.flush();
    } finally {
      mutator.close();
    }
    return System.currentTimeMillis() - currentTime;
  }

  /**
   * 异步添加单条数据 就是把缓冲池填满在一哈添加到hbase中
   *
   * @param tableName
   * @param put
   * @return
   * @throws Exception
   */
  public long asynPut(String tableName, Put put) throws Exception {
    return asynPut(tableName, Arrays.asList(put));
  }

  /**
   * 一条一条数据添加，不走缓冲池
   *
   * @param tableName
   * @param put
   * @return
   */
  public long synPut(String tableName, Put put) {
    long currentTime = System.currentTimeMillis();
    Table table = getTable(tableName);
    if (table != null) {
      try {
        table.put(put);
      } catch (IOException e) {
        LOGGER.error("同步添加数据:{}失败", put.getRow(), e);
      } finally {
        try {
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return System.currentTimeMillis() - currentTime;
  }

  /**
   * 一次添加一批数据
   *
   * @param tableName
   * @param puts
   * @return
   */
  public long synPuts(String tableName, List<Put> puts) {
    long currentTime = System.currentTimeMillis();
    Table table = getTable(tableName);
    if (table != null) {
      try {
        table.put(puts);
      } catch (IOException e) {
        LOGGER.error("同步批量添加数据:{}失败", e);
      } finally {
        try {
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return System.currentTimeMillis() - currentTime;
  }

  /**
   * 删除某行数据
   *
   * @param tableName
   * @param row
   */
  public void delete(String tableName, String row) {
    Table table = getTable(tableName);
    if (table != null) {
      try {
        Delete delete = new Delete(row.getBytes());
        table.delete(delete);
      } catch (IOException e) {
        LOGGER.error("删除数据:{}失败", row, e);
      } finally {
        try {
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 批量删除数据
   *
   * @param tableName
   * @param rows
   */
  public void delete(String tableName, String[] rows) {
    Table table = getTable(tableName);
    if (table != null) {
      try {
        List<Delete> deletes = new ArrayList<>();
        Arrays.stream(rows).forEach(row -> deletes.add(new Delete(row.getBytes())));
        if (deletes.size() > 0) table.delete(deletes);
      } catch (IOException e) {
        LOGGER.error("批量删除数据失败", e);
      } finally {
        try {
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 获取单条数据
   *
   * @param tableName
   * @param row
   * @param filters
   * @return
   */
  public Result getRow(String tableName, String row, Filter... filters) {
    Table table = getTable(tableName);
    Result rs = null;
    if (table != null) {
      try {
        Get get = new Get(row.getBytes());
        if (filters.length > 0) {
          Arrays.stream(filters).forEach(filter -> get.setFilter(filter));
        }
        rs = table.get(get);
      } catch (IOException e) {
        LOGGER.error("获取数据:{}失败", row, e);
      } finally {
        try {
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return rs;
  }

  /**
   * 批量获取数据
   *
   * @param tableName
   * @param rows
   * @return
   */
  public Result[] getRows(String tableName, List<String> rows) {
    Table table = getTable(tableName);
    Result[] results = null;
    if (table != null) {
      List<Get> gets = new ArrayList<>();
      try {
        rows.stream()
            .filter(s -> !StringUtils.isBlank(s))
            .forEach(s -> gets.add(new Get(s.getBytes())));
        if (gets.size() > 0) results = table.get(gets);
      } catch (IOException e) {
        LOGGER.error("获取数据失败", e);
      } finally {
        try {
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return results;
  }

  public ResultScanner getScan(
      String tableName, HashMap<String, List<String>> paramHashMap, Filter filter) {
    Table table = getTable(tableName);
    ResultScanner results = null;
    if (table != null) {
      try {
        if (paramHashMap == null) {
          paramHashMap = new HashMap<>();
        }
        results = table.getScanner(setScanParam(paramHashMap, filter));
      } catch (IOException e) {
        LOGGER.error("获取扫描器失败", e);
      } finally {
        try {
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return results;
  }

  private Scan setScanParam(HashMap<String, List<String>> paramHashMap, Filter filter) {
    Scan scan = new Scan();
    scan.setCaching(1000);
    if (filter != null) {
      scan.setFilter(filter);
    }
    if (paramHashMap.containsKey("column")) {
      paramHashMap
          .get("column")
          .forEach(s -> scan.addColumn(s.split("-")[0].getBytes(), s.split("-")[1].getBytes()));
    }
    if (paramHashMap.containsKey("timeRange")) {
      String timeRange = paramHashMap.get("timeRange").get(0);
      try {
        scan.setTimeRange(
            Long.parseLong(timeRange.split("-")[0]), Long.parseLong(timeRange.split("-")[1]));
      } catch (IOException e) {
        LOGGER.error("设置扫描器时间失败", e);
      }
    }
    if (paramHashMap.containsKey("timeStamp")) {
      try {
        scan.setTimeStamp(Long.parseLong(paramHashMap.get("timeStamp").get(0)));
      } catch (IOException e) {
        LOGGER.error("设置扫描器时间失败");
      }
    }
    if (paramHashMap.containsKey("version")) {
      scan.setMaxVersions(Integer.parseInt(paramHashMap.get("version").get(0)));
    }
    if (paramHashMap.containsKey("startRow")) {
      scan.setStartRow(paramHashMap.get("startRow").get(0).getBytes());
    }
    if (paramHashMap.containsKey("stopRow")) {
      scan.setStopRow(paramHashMap.get("stopRow").get(0).getBytes());
    }
    if (paramHashMap.containsKey("families")) {
      paramHashMap.get("families").stream().forEach(s -> scan.addFamily(s.getBytes()));
    }
    return scan;
=======
    return 0;
>>>>>>> parent of ab5639f... hbase1
  }
}
