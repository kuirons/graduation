package com.graduation.data.extrabean;

import java.util.List;

/** 这个是前端请求用户数据用的 Created by kuirons on 18-4-28 */
public class UserData {
  private String showName;
  private String phone;
  private List<String> roleInfos;
  private String description;

  public UserData setDescription(String description) {
    this.description = description;
    return this;
  }

  public UserData setRoleInfos(List<String> roleInfos) {

    this.roleInfos = roleInfos;
    return this;
  }

  public UserData setPhone(String phone) {

    this.phone = phone;
    return this;
  }

  public UserData setShowName(String showName) {

    this.showName = showName;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public List<String> getRoleInfos() {

    return roleInfos;
  }

  public String getPhone() {

    return phone;
  }

  public String getShowName() {

    return showName;
  }

  //    public static void main(String[] args) {
  //    List<String> roleinfos = new ArrayList<>();
  //    roleinfos.add("sdf");
  //    roleinfos.add("asdfsa");
  //    System.out.println(
  //        JSON.toJSONString(
  //            new UserData()
  //                .setDescription("1111")
  //                .setPhone("123123123")
  //                .setRoleInfos(roleinfos)
  //                .setShowName("aaaaa")));
  //  }
}
