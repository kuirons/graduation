package com.graduation.logic.user;

import com.alibaba.fastjson.JSON;
import com.graduation.data.bean.UserBean;
import com.graduation.logic.db.impl.UserDBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WuGYu
 * @date 2018/4/27 17:28
 */
@Service
public class UserManager {
  @Autowired UserDBImpl userDB;

  /** @return json */
  public String getAllUserInfo() {
    List<UserBean> allUser = userDB.getAllUser();
    if (allUser == null || allUser.isEmpty()) return null;
    return JSON.toJSONString(allUser);
  }
}
