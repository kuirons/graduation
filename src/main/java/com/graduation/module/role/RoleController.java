package com.graduation.module.role;

import com.alibaba.fastjson.JSON;
import com.graduation.data.bean.RoleBean;
import com.graduation.logic.db.impl.RoleDBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author WuGYu
 * @date 2018/4/28 17:34
 */
@Controller
@RequestMapping("/role")
public class RoleController {
  @Autowired RoleDBImpl roleDBImpl;

  @RequestMapping("/getRoleData")
  @ResponseBody
  public String getAllRoleData() {
      List<RoleBean> roleInfos = roleDBImpl.getAllRoleInfos();
      return JSON.toJSONString(roleInfos);
  }
}
