package com.graduation.logic.role;

import com.graduation.data.bean.UserRoleBean;
import com.graduation.logic.db.impl.UserRoleDBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** Created by kuirons on 18-4-28 */
@Service
public class RoleManager {
  @Autowired UserRoleDBImpl userRoleDBImpl;

  public List<UserRoleBean> getRolesByUserName(String userName) {
    List<UserRoleBean> roleInfos = userRoleDBImpl.getUserRoleByUserName(userName);
    return roleInfos;
  }

  public boolean updateUserRoleInfos(String[] changeRoleInfos, String username) {
    return userRoleDBImpl.updateUserRoleInfos(changeRoleInfos, username);
  }

  public boolean addUserRoleInfos(String[] addRoleInfos, String username) {
    return userRoleDBImpl.addUserInfos(addRoleInfos, username);
  }
}
