package com.graduation.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * @author WuGYu
 * @date 2018/3/20 21:26
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
  protected Class<?>[] getRootConfigClasses() {
    return new Class[] {RootConfig.class, SecurityConfig.class};
  }

  protected Class<?>[] getServletConfigClasses() {
    return new Class[] {WebConfig.class};
  }

  protected String[] getServletMappings() {
    return new String[] {"/"};
  }

  @Override
  protected Filter[] getServletFilters() {
    // 默认不是utf-8，还得自己转
    return new Filter[] {new CharacterEncodingFilter("UTF-8", true, true)};
  }
}
