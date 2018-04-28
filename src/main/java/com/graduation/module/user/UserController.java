package com.graduation.module.user;

import com.alibaba.fastjson.JSON;
import com.graduation.data.extrabean.UserData;
import com.graduation.logic.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author WuGYu
 * @date 2018/4/27 17:22
 */
@Controller
@RequestMapping("/user")
public class UserController {
  @Autowired UserManager userManager;

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
  public String changeUserInfos(
      @RequestParam(value = "changeRoleInfos[]", required = false) String[] changeRoleInfos,
      @RequestParam(value = "changephone") String changePhone,
      @RequestParam(value = "changeDescription") String changeDescription) {
    for (int i = 0; i < changeRoleInfos.length; i++) {
      System.out.println(changeRoleInfos[i]);
    }
    return null;
  }
}
