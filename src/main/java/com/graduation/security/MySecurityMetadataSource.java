package com.graduation.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.Map;

/**
 * @author WuGYu
 * @date 2018/4/8 17:38
 */
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
  @Override
  public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
    FilterInvocation filterInvocation = (FilterInvocation) o;
    Map<String, Collection<ConfigAttribute>> metadataSource = MySecurityContext.getMetadataSource();
    for (Map.Entry entry : metadataSource.entrySet()) {
      String url = (String) entry.getKey();
      RequestMatcher requestMatcher = new AntPathRequestMatcher(url);
      if (requestMatcher.matches(filterInvocation.getRequest())) {
        return (Collection<ConfigAttribute>) entry.getValue();
      }
    }
    // 未配置的url不需要权限，也就是说默认白名单
    return null;
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return null;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return FilterInvocation.class.isAssignableFrom(aClass);
  }
}
