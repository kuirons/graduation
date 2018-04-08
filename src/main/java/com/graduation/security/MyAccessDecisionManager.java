package com.graduation.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author WuGYu
 * @date 2018/4/8 15:38
 */
public class MyAccessDecisionManager implements AccessDecisionManager {
  // 或
  @Override
  public void decide(
      Authentication authentication, Object o, Collection<ConfigAttribute> collection)
      throws AccessDeniedException, InsufficientAuthenticationException {
    if (authentication == null) throw new AccessDeniedException("没有访问权限");
    // 需要的权限
    Iterator<ConfigAttribute> iterator = collection.iterator();
    while (iterator.hasNext()) {
      ConfigAttribute configAttribute = iterator.next();
      String needCode = configAttribute.getAttribute();

      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      for (GrantedAuthority authority : authorities) {
        if (StringUtils.equals(authority.getAuthority(), "ROLE_" + needCode)) return;
      }
    }
    throw new AccessDeniedException("没有访问权限");
  }

  @Override
  public boolean supports(ConfigAttribute configAttribute) {
    return true;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return true;
  }
}
