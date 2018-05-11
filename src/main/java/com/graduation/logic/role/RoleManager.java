package com.graduation.logic.role;

import com.graduation.data.bean.RoleBean;
import com.graduation.data.bean.UserRoleBean;
import com.graduation.data.extrabean.RoleData;
import com.graduation.logic.db.impl.RoleDBImpl;
import com.graduation.logic.db.impl.RoleJurisdicationDBImpl;
import com.graduation.logic.db.impl.UserRoleDBImpl;
import com.graduation.logic.jurisdiction.JurisdictionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** Created by kuirons on 18-4-28 */
@Service
public class RoleManager {
  @Autowired UserRoleDBImpl userRoleDBImpl;
  @Autowired RoleDBImpl roleDB;
  @Autowired RoleJurisdicationDBImpl roleJurisdicationDB;
  @Autowired JurisdictionManager jurisdictionManager;

  public List<UserRoleBean> getRolesByUserName(String userName) {
    List<UserRoleBean> roleInfos = userRoleDBImpl.getUserRoleByUserName(userName);
    return roleInfos;
  }

  public boolean updateUserRoleInfos(String[] changeRoleInfos, String username) {
    return userRoleDBImpl.updateUserRoleInfos(changeRoleInfos, username);
  }

  public boolean addUserRoleInfos(String[] addRoleInfos, String username) {
    return userRoleDBImpl.addUserRoleInfos(addRoleInfos, username);
  }

  public List<RoleData> getRoleData() {
    return roleDB.getRoleData();
  }

  public List<RoleBean> getAllRoleInfos() {
    return roleDB.getAllRoleInfos();
  }

  public boolean changeRoleInfos(
      String newRoleDescription, String[] changePermissionInfos, String changeRoleName) {
    if (!roleDB.updateRoleInfos(changeRoleName, newRoleDescription)
        || !roleJurisdicationDB.updataRoleJurisdictions(changeRoleName, changePermissionInfos))
      return false;
    return true;
  }

  public boolean addRoleInfos(String roleName, String roleDescription, String[] addPermissions) {
    if (!roleDB.addRoleInfos(roleDescription, roleName)
        || !roleJurisdicationDB.addRoleJurisdiction(addPermissions, roleName)) return false;
    return true;
  }

  public void deleteRole(String deleteName) {
    // todo 这里删除要添加限制
    // 这里还是要删除两项
    roleDB.deleteRole(deleteName);
    roleJurisdicationDB.deletePermissionByUsername(deleteName);
  }

  public RoleData searchRoleInfos(String roleName) {
    return roleDB.getRoleDataByName(roleName);
  }
}
