package com.graduation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import java.io.IOException;

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

  @Bean
  public MultipartResolver multipartResolver() throws IOException {
    return new StandardServletMultipartResolver();
  }

  @Override
  protected Filter[] getServletFilters() {
    // 默认不是utf-8，还得自己转
    return new Filter[] {new CharacterEncodingFilter("UTF-8", true, true)};
  }

  // 文件上传相关配置
  @Override
  protected void customizeRegistration(ServletRegistration.Dynamic registration) {
    registration.setMultipartConfig(
        // 默认上传到程序的发布目录
        new MultipartConfigElement(""));
  }
}
