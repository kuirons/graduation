package com.graduation.data.bean;

/** Created by kuirons on 18-4-7 */
public class UserBean {
  private String userName;
  private String password;

  public String getPassword() {
    return password;
  }

  public UserBean setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getUserName() {

    return userName;
  }

  public UserBean setUserName(String userName) {
    this.userName = userName;
    return this;
  }
}
