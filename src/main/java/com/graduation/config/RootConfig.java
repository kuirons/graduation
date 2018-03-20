package com.graduation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 这里主要用来管理不与前端进行交互的bean
 *
 * @author WuGYu
 * @date 2018/3/19 20:42
 */
@Configuration
@ComponentScan(basePackages = {"com.graduation.log", "com.graduation.logic", "com.graduation.util"})
public class RootConfig {}
