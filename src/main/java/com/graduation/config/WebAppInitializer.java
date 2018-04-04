package com.graduation.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author WuGYu
 * @date 2018/3/20 21:26
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
  protected Class<?>[] getRootConfigClasses() {
    return new Class[] {RootConfig.class,SecurityConfig.class};
  }

  protected Class<?>[] getServletConfigClasses() {
    return new Class[] {WebConfig.class};
  }

  protected String[] getServletMappings() {
    return new String[] {"/"};
  }

}
