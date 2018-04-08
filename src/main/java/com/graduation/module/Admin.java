package com.graduation.module;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WuGYu
 * @date 2018/4/8 21:20
 */
@Controller
public class Admin {
  @RequestMapping("/admin")
  @ResponseBody
  public Object test() {
    List<String> result = new ArrayList<>();
    result.add("afasdfsadf");
    result.add("adgfd");
    return result;
  }
}
