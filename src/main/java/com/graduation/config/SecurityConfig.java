package com.graduation.config;

import com.graduation.security.MyAccessDecisionManager;
import com.graduation.security.MySaltSource;
import com.graduation.security.MySecurityMetadataSource;
import com.graduation.security.MyUserDetailsServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 创建过滤器
 *
 * @author WuGYu
 * @date 2018/4/4 14:43
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  // salt:graduation
  @Bean
  public SaltSource saltSource() {
    return new MySaltSource();
  }

  @Bean
  public AccessDecisionManager accessDecisionManager() {
    return new MyAccessDecisionManager();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new MyUserDetailsServer();
  }

  @Bean
  public MySecurityMetadataSource mySecurityMetadataSource() {
    return new MySecurityMetadataSource();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(new Md5PasswordEncoder());
    authenticationProvider.setSaltSource(saltSource());
    return authenticationProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
    //    auth.inMemoryAuthentication().withUser("user").password("user").roles("ROLE_USER");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 这些请求不验证
    http.authorizeRequests()
        // 允许登陆
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .loginPage("/static/login.html")
        .loginProcessingUrl("/login")
        .usernameParameter("username")
        .passwordParameter("password")
        .defaultSuccessUrl("/static/index.html", true)
        .failureUrl("/static/login.html")
        .permitAll()
        .and()
        .logout()
        .permitAll()
        .and()
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .withObjectPostProcessor(
            new ObjectPostProcessor<FilterSecurityInterceptor>() {
              @Override
              public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                o.setAccessDecisionManager(accessDecisionManager());
                o.setSecurityMetadataSource(new MySecurityMetadataSource());
                return o;
              }
            })
        .and()
        .csrf()
        .disable();
  }
}
