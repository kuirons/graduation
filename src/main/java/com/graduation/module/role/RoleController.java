package com.graduation.module.role;

import com.alibaba.fastjson.JSON;
import com.graduation.data.bean.RoleBean;
import com.graduation.data.extrabean.RoleData;
import com.graduation.logic.db.impl.RoleDBImpl;
import com.graduation.logic.role.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author WuGYu
 * @date 2018/4/28 17:34
 */
@Controller
@RequestMapping("/role")
public class RoleController {
  @Autowired RoleManager roleManager;

  @RequestMapping(value = "/getAllRoleData", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getAllRoleData() {
    List<RoleData> roleInfos = roleManager.getRoleData();
    return JSON.toJSONString(roleInfos);
  }

  @RequestMapping(value = "/getRoleData", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getRoleData() {
    List<RoleBean> roleInfos = roleManager.getAllRoleInfos();
    return JSON.toJSONString(roleInfos);
  }

  @RequestMapping(value = "/saveChangeRoleName", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public void saveChangeUserName(
      @RequestParam(value = "changerolename") String changeRoleName,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession();
    session.setAttribute("changeRoleName", changeRoleName.trim());
    PrintWriter out = response.getWriter();
    out.write("{\"status\":\"success\"}");
    out.flush();
    out.close();
  }

  // todo 如果为空则表示不更改,这里其实有问题，需要上锁
  @RequestMapping(value = "/changeRoleInfos", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public void changeRoleInfos(
      @RequestParam(value = "changePermissionInfos[]", required = false)
          String[] changePermissionInfos,
      @RequestParam(value = "newroledescription", required = false) String newRoleDescription,
      HttpServletResponse response,
      HttpServletRequest request)
      throws IOException {
    if (!roleManager.changeRoleInfos(
        newRoleDescription,
        changePermissionInfos,
        (String) request.getSession().getAttribute("changeRoleName"))) response.setStatus(500);
    else {
      // 这里直接返回字符串是不行的
      PrintWriter out = response.getWriter();
      out.write("{\"status\":\"success\"}");
      out.flush();
      out.close();
    }
  }
}
