package com.graduation.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author WuGYu
 * @date 2018/4/8 13:49
 */
public class MyUserDetails implements UserDetails {
  private String userName;
  private String password;
  private boolean enabled;
  private boolean isAccountNonExpired;
  private Collection<? extends GrantedAuthority> authorities;

  public MyUserDetails(
      String userName, String password, boolean enabled, boolean isAccountNonExpired) {
    super();
    this.userName = userName;
    this.password = password;
    this.enabled = enabled;
    this.isAccountNonExpired = isAccountNonExpired;
  }

  public MyUserDetails(
      String userName,
      String password,
      boolean enabled,
      boolean isAccountNonExpired,
      Collection<? extends GrantedAuthority> authorities) {
    super();
    this.userName = userName;
    this.password = password;
    this.enabled = enabled;
    this.isAccountNonExpired = isAccountNonExpired;
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isAccountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
