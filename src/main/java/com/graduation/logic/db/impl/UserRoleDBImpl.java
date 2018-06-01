package com.graduation.logic.db.impl;

import com.graduation.data.bean.UserRoleBean;
import com.graduation.logic.db.HbaseManager;
import com.graduation.util.CommonUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author WuGYu
 * @date 2018/4/8 14:42
 */
@Repository
public class UserRoleDBImpl {
  private static final String TABLENAME = "graduation_user_role";
  @Autowired HbaseManager hbaseManager;

  public List<UserRoleBean> getUserRoleByUserName(String userName) {
    List<UserRoleBean> userRoleBeans = new ArrayList<>();
    // 先构造一个前缀匹配器
    PrefixFilter filter = new PrefixFilter(userName.getBytes());
    ResultScanner scanner = hbaseManager.getScan(TABLENAME, null, filter);
    scanner.forEach(result -> userRoleBeans.add(new UserRoleBean(new String(result.getRow()))));
    scanner.close();
    return userRoleBeans;
  }

  /**
   * 这个更新比较烦，需要先把原来的删除，然后插入新的数据
   *
   * @param changeRoleInfos
   * @return
   */
  public boolean updateUserRoleInfos(String[] changeRoleInfos, String username) {
    // 如果changeRoleInfos为空，表示没有选择任何权限
    deleteByUsername(username);
    return addUserRoleInfos(changeRoleInfos, username);
  }

  public boolean addUserRoleInfos(String[] changeRoleInfos, String username) {
    if (!(changeRoleInfos == null || changeRoleInfos.length <= 0)) {
      Arrays.stream(changeRoleInfos)
          .map(s -> new UserRoleBean(username, s).toString())
          .forEach(
              s -> {
                Put put = new Put(s.getBytes());
                // 这个单纯就是用来做标记的
                put.addColumn("info".getBytes(), "description".getBytes(), "aa".getBytes());
                hbaseManager.synPut(TABLENAME, put);
              });
      return true;
    }
    return false;
  }

  public void deleteByUsername(String username) {
    List<UserRoleBean> oldInfos = getUserRoleByUserName(username);
    oldInfos
        .stream()
        .map(userRoleBean -> userRoleBean.toString())
        .forEach(s -> hbaseManager.delete(TABLENAME, s));
  }

  // 当删除一个角色的时候，同时要在用户角色表中删除包含该角色的条目
  // todo 这个还没测
  public void deleteRoleByRoleName(String deleteName) {
    // 先构造个比较器,大小写不敏感
    SubstringComparator comparator = new SubstringComparator(deleteName);
    RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, comparator);
    ResultScanner scanner = hbaseManager.getScan(TABLENAME, null, filter);
    scanner.forEach(
        result -> hbaseManager.delete(TABLENAME, CommonUtil.bytesToString(result.getRow())));
  }
}
