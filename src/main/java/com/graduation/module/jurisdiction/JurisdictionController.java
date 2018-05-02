package com.graduation.module.jurisdiction;

import com.alibaba.fastjson.JSON;
import com.graduation.data.bean.JurisdictionBean;
import com.graduation.logic.db.impl.JurisdictionDBImpl;
import com.graduation.logic.jurisdiction.JurisdictionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/** Created by kuirons on 18-5-3 */
@Controller
@RequestMapping("/jurisdiction")
public class JurisdictionController {
  @Autowired
  JurisdictionManager jurisdictionManager;

  @RequestMapping(value = "/getAllPermission", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getAllPermission() {
      List<JurisdictionBean> jurisdictionBeans = jurisdictionManager.getAllPermissions();
      return JSON.toJSONString(jurisdictionBeans);
  }
}
