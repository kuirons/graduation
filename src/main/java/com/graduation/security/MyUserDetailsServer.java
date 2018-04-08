package com.graduation.security;

import com.graduation.data.bean.UserBean;
import com.graduation.data.bean.UserRoleBean;
import com.graduation.logic.db.impl.RoleJurisdicationDBImpl;
import com.graduation.logic.db.impl.UserDBImpl;
import com.graduation.logic.db.impl.UserRoleDBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** Created by kuirons on 18-4-7 */
public class MyUserDetailsServer implements UserDetailsService {
  @Autowired UserDBImpl userDB;
  @Autowired UserRoleDBImpl userRoleDB;
  @Autowired RoleJurisdicationDBImpl roleJurisdicationDB;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    UserBean userbean = userDB.getUserByName(userName);
    if (userbean == null) throw new UsernameNotFoundException(userName + "找不到啊！！！！！");
    List<UserRoleBean> urs = userRoleDB.getUserRoleByUserName(userName);
    List<String> roleNames = new ArrayList<>();
    urs.forEach(userRoleBean -> roleNames.add(userRoleBean.getRoleName()));
    // 获取权限集合
    List<Jurisdiction> jurisdictions = roleJurisdicationDB.getPermissionByRoleName(roleNames);
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    // 组装成权限列表
    jurisdictions.forEach(
        jurisdiction -> {
          // 这个前缀很诡异，如果有错就去掉
          GrantedAuthority grantedAuthority =
              new SimpleGrantedAuthority("ROLE_" + jurisdiction.name());
          grantedAuthorities.add(grantedAuthority);
        });
    return new MyUserDetails(
        userbean.getUserName(), userbean.getPassword(), true, true, grantedAuthorities);
  }
}
