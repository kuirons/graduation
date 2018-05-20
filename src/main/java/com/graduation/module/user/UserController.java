package com.graduation.module.user;

import com.alibaba.fastjson.JSON;
import com.graduation.data.extrabean.UserData;
import com.graduation.logic.log.LogManager;
import com.graduation.logic.user.UserManager;
import com.graduation.module.user.log.AddUserLog;
import com.graduation.module.user.log.ChangeUserLog;
import com.graduation.module.user.log.DeleteUserLog;
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
 * @date 2018/4/27 17:22
 */
@Controller
@RequestMapping("/user")
public class UserController {
  @Autowired UserManager userManager;
  @Autowired LogManager logManager;

  @RequestMapping(value = "/getUserData", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getUserData(HttpServletResponse response) {
    List<UserData> allUserInfo = userManager.getAllUserInfo();
    // 哎呀这里肯定是有问题，就不处理了
    if (allUserInfo == null) return null;
    return JSON.toJSONString(allUserInfo);
  }

  @RequestMapping(value = "/changeUserInfos", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public void changeUserInfos(
      @RequestParam(value = "changeRoleInfos[]", required = false) String[] changeRoleInfos,
      @RequestParam(value = "changePhone", required = false) String changePhone,
      @RequestParam(value = "changeDescription", required = false) String changeDescription,
      HttpServletResponse response,
      HttpServletRequest request)
      throws IOException {
    // 记录日志
    logManager.log(
        new ChangeUserLog(
            (String) request.getSession().getAttribute("username"),
            (String) request.getSession().getAttribute("changeUserName"),
            JSON.toJSONString(changeRoleInfos),
            changePhone));
    if (!userManager.changeUserInfos(
        changePhone,
        changeRoleInfos,
        changeDescription,
        (String) request.getSession().getAttribute("changeUserName"))) response.setStatus(500);
    else {
      // 这里直接返回字符串是不行的
      PrintWriter out = response.getWriter();
      out.write("{\"status\":\"success\"}");
      out.flush();
      out.close();
    }
  }

  @RequestMapping(value = "/addUserInfos", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public void addUserInfos(
      @RequestParam(value = "addRoleInfos[]", required = false) String[] addRoleInfos,
      @RequestParam(value = "addphonenum", required = false) String addPhonenum,
      @RequestParam(value = "adduserdescription", required = false) String addUserDescription,
      @RequestParam(value = "addusername") String userName,
      @RequestParam(value = "adduserpassword") String addUserPassword,
      HttpServletResponse response,
      HttpServletRequest request)
      throws IOException {
    // 记录日志
    logManager.log(
        new AddUserLog(
            (String) request.getSession().getAttribute("username"),
            userName,
            JSON.toJSONString(addRoleInfos),
            addPhonenum));
    if (!userManager.addNewUserInfos(
        userName, addPhonenum, addUserDescription, addRoleInfos, addUserPassword))
      response.setStatus(500);
    else {
      // 这里直接返回字符串是不行的
      PrintWriter out = response.getWriter();
      out.write("{\"status\":\"success\"}");
      out.flush();
      out.close();
    }
  }

  @RequestMapping(value = "/saveChangeUserName", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public void saveChangeUserName(
      @RequestParam(value = "changeusername") String changeUserName,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    HttpSession session = request.getSession();
    session.setAttribute("changeUserName", changeUserName.trim());
    // 记录日志
    logManager.log(
        new DeleteUserLog((String) session.getAttribute("username"), changeUserName.trim()));
    PrintWriter out = response.getWriter();
    out.write("{\"status\":\"success\"}");
    out.flush();
    out.close();
  }

  @RequestMapping(value = "/delteUserInfos", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public void delteUserInfos(
      @RequestParam(value = "deleteusername") String deleteUsername, HttpServletResponse response)
      throws IOException {
    userManager.deleteUserinfos(deleteUsername);
    PrintWriter out = response.getWriter();
    out.write("{\"status\":\"success\"}");
    out.flush();
    out.close();
  }
}
