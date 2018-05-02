package com.graduation.data.extrabean;

import java.util.List;

/**
 * @author WuGYu
 * @date 2018/5/2 21:46
 */
public class RoleData {
  private String roleName;
  private String description;
  private List<String> jurisdictions;

  public List<String> getJurisdictions() {
    return jurisdictions;
  }

  public RoleData setJurisdictions(List<String> jurisdictions) {
    this.jurisdictions = jurisdictions;
    return this;
  }

  public String getDescription() {

    return description;
  }

  public RoleData setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getRoleName() {

    return roleName;
  }

  public RoleData setRoleName(String roleName) {
    this.roleName = roleName;
    return this;
  }
}
