package com.graduation.module.role;

import com.alibaba.fastjson.JSON;
import com.graduation.data.bean.RoleBean;
import com.graduation.data.extrabean.RoleData;
import com.graduation.logic.log.LogManager;
import com.graduation.logic.role.RoleManager;
import com.graduation.module.role.log.AddRoleLog;
import com.graduation.module.role.log.ChangeRoleLog;
import com.graduation.module.role.log.DeleteRoleLog;
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
  @Autowired LogManager logManager;

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
  public void saveChangeRoleName(
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

  @RequestMapping(value = "/changeRoleInfos", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public void changeRoleInfos(
      @RequestParam(value = "changePermissionInfos[]", required = false)
          String[] changePermissionInfos,
      @RequestParam(value = "newroledescription", required = false) String newRoleDescription,
      HttpServletResponse response,
      HttpServletRequest request)
      throws IOException {
    // 记录日志
    logManager.log(
        new ChangeRoleLog(
            (String) request.getSession().getAttribute("username"),
            (String) request.getSession().getAttribute("changeRoleName"),
            JSON.toJSONString(changePermissionInfos)));
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

  @RequestMapping(value = "/addRoleInfos", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public void addRoleInfos(
      @RequestParam(value = "addPermissionInfos[]", required = false) String[] addPermissions,
      @RequestParam(value = "new-add-role-name", required = false) String roleName,
      @RequestParam(value = "new-add-role-description", required = false) String roleDescription,
      HttpServletResponse response,
      HttpServletRequest request)
      throws IOException {
    // 记录日志
    logManager.log(
        new AddRoleLog(
            (String) request.getSession().getAttribute("username"),
            roleName,
            JSON.toJSONString(addPermissions)));
    if (!roleManager.addRoleInfos(roleName, roleDescription, addPermissions))
      response.setStatus(500);
    else {
      // 这里直接返回字符串是不行的
      PrintWriter out = response.getWriter();
      out.write("{\"status\":\"success\"}");
      out.flush();
      out.close();
    }
  }

  @RequestMapping(value = "/deleteRole", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public void deleteRole(
      @RequestParam(value = "deleteRoleName") String deleteName,
      HttpServletResponse response,
      HttpServletRequest request)
      throws IOException {
    // 记录日志
    logManager.log(
        new DeleteRoleLog((String) request.getSession().getAttribute("username"), deleteName));
    roleManager.deleteRole(deleteName);
    PrintWriter out = response.getWriter();
    out.write("{\"status\":\"success\"}");
    out.flush();
    out.close();
  }

  @RequestMapping(value = "/search", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String search(@RequestParam(value = "role-search-name") String roleName) {
    RoleData object = roleManager.searchRoleInfos(roleName);
    if (object == null) return "{\"status\":\"fail\"}";
    return JSON.toJSONString(object);
  }
}
