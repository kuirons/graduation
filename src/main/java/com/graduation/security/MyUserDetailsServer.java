package com.graduation.security;

import com.graduation.logic.db.HbaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Created by kuirons on 18-4-7 */
@Service
public class MyUserDetailsServer implements UserDetailsService {
  @Autowired HbaseManager hbaseManager;

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    return null;
  }
}
