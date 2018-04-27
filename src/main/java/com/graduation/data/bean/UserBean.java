package com.graduation.data.bean;

/** Created by kuirons on 18-4-7 */
public class UserBean {
  private String userName;
  private String password;
  private String showName;
  private String phone;
  private String description;

  public String getDescription() {
    return description;
  }

  public UserBean setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getPhone() {

    return phone;
  }

  public UserBean setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public String getShowName() {

    return showName;
  }

  public UserBean setShowName(String showName) {
    this.showName = showName;
    return this;
  }

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
