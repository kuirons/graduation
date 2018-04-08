package com.graduation.security;

import com.graduation.data.bean.UserBean;
import com.graduation.logic.db.impl.UserDBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Created by kuirons on 18-4-7 */
@Service
public class MyUserDetailsServer implements UserDetailsService {
  @Autowired UserDBImpl userDB;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    UserBean userbean = userDB.getUserByName(userName);
    return null;
  }
}
