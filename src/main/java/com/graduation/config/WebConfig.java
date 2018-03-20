package com.graduation.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @author WuGYu
 * @date 2018/3/19 20:47
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.graduation.module")
public class WebConfig extends WebMvcConfigurerAdapter {
  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    // 不放在web-inf文件夹下，通过其它方式来保证访问安全
    resolver.setPrefix("/");
    // 使用html网页
    resolver.setSuffix(".html");
    return resolver;
  }

  // todo 如果需要对静态资源进行访问，可以在下面进行配置
//  @Override
//  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {}
}
