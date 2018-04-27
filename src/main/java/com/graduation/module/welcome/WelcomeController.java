package com.graduation.module.welcome;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 首页
 *
 * @author WuGYu
 * @date 2018/4/25 14:32
 */
@Controller
public class WelcomeController {
  @RequestMapping(value = "/")
  public void welcome(HttpServletResponse response) throws IOException {
    response.sendRedirect("/dataAnalysis/static/login.html");
  }
}
