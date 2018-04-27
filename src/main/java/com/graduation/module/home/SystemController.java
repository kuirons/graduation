package com.graduation.module.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 根据权限控制前端系统管理标签的显示
 * @author WuGYu
 * @date 2018/4/27 16:13
 */
@Controller
public class SystemController {
  @RequestMapping("/home/systemControl")
  public void systemControl(HttpServletResponse response) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.write("{\"status\":\"success\"}");
    out.flush();
    out.close();
  }
}
