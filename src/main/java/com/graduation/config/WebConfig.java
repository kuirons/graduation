package com.graduation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author WuGYu
 * @date 2018/3/19 20:47
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.graduation.module")
public class WebConfig extends WebMvcConfigurerAdapter {
  // 现在把html当静态资源处理，如果是在是要做html的解析，还要在这里配置html的视图解析器
  // 如果需要对静态资源进行访问，可以在下面进行配置
  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }
}
