package com.graduation.logic.user;

import com.alibaba.fastjson.JSON;
import com.graduation.data.bean.UserBean;
import com.graduation.data.extrabean.UserData;
import com.graduation.logic.db.impl.UserDBImpl;
import com.graduation.logic.role.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WuGYu
 * @date 2018/4/27 17:28
 */
@Service
public class UserManager {
  @Autowired UserDBImpl userDB;
  @Autowired RoleManager roleManager;

  /** @return json */
  public  List<UserData> getAllUserInfo() {
    List<UserData> userDatas = new ArrayList<>();
    List<UserBean> allUser = userDB.getAllUser();
    allUser.forEach(
        userBean -> {
          UserData userData = new UserData();
          List<String> roles = roleManager.getRolesByUserName(userBean.getUserName());
          userData.setPhone(userBean.getPhone());
          userData.setDescription(userBean.getDescription());
          userData.setRoleInfos(roles);
          userData.setUserName(userBean.getUserName());
          userDatas.add(userData);
        });
    if (userDatas == null || userDatas.isEmpty()) return null;
    return userDatas;
  }
}
