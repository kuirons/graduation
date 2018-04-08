package com.graduation.security;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 加盐
 *
 * @author WuGYu
 * @date 2018/4/8 16:29
 */
public class MySaltSource implements SaltSource {
  @Override
  public Object getSalt(UserDetails userDetails) {
    return "graduation";
  }
}
