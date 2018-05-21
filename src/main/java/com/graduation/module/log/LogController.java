package com.graduation.module.log;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author WuGYu
 * @date 2018/5/21 16:38
 */
@Controller
@RequestMapping("/log")
public class LogController {
  @RequestMapping(value = "/getSystemLog", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getSystemLog() throws IOException {
    List<String> logs;
    File file = new File("target/logs/ltnz.log");
    // 这里必须指定为utf-8
    logs = FileUtils.readLines(file, "UTF-8");
    return JSON.toJSONString(logs);
  }

  @RequestMapping(value = "/getOperationLog", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getOperationLog() throws IOException {
    List<String> logs;
    File file = new File("target/running_log/RUNNING_LOG.log");
    // 这里必须指定为utf-8
    logs = FileUtils.readLines(file, "UTF-8");
    return JSON.toJSONString(logs);
  }
}
