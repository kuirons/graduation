package com.graduation.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author WuGYu
 * @date 2018/4/23 21:51
 */
public class MyAccessDenieHandler implements AccessDeniedHandler {
  @Override
  public void handle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AccessDeniedException e)
      throws IOException, ServletException {
    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    httpServletResponse.setContentType("application/json;charset=UTF-8");
    PrintWriter out = httpServletResponse.getWriter();
    out.write("{\"status\":\"error\",\"msg\":\"权限不足，请联系管理员!\"}");
    out.flush();
    out.close();
  }
}
