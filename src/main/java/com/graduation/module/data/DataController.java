package com.graduation.module.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.graduation.data.bean.DataBean;
import com.graduation.data.bean.DataGBean;
import com.graduation.data.bean.MessageBean;
import com.graduation.logic.data.DataManager;
import com.graduation.logic.log.LogManager;
import com.graduation.module.data.log.CheckDataLog;
import com.graduation.module.data.log.CommentLog;
import com.graduation.module.data.log.DataUploadLog;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/data")
public class DataController {
  @Autowired DataManager dataManager;
  @Autowired LogManager logManager;

  @RequestMapping(
    value = "/upload",
    method = RequestMethod.POST,
    produces = {"application/json;charset=UTF-8"}
  )
  @ResponseBody
  public String upload(
      @RequestParam(value = "files") MultipartFile multipartFile,
      @RequestParam(value = "importdata-description") String description,
      @RequestParam(value = "newfilename") String filename,
      HttpServletRequest request)
      throws IOException {
    JSONObject res = new JSONObject();
    if (!multipartFile.isEmpty()) {
      //      String suffix = multipartFile.getOriginalFilename().split(".")[1];
      String suffix = ".ocamdq_gv";
      FileUtils.copyInputStreamToFile(
          multipartFile.getInputStream(), new File("target/upload", filename + suffix));
      HttpSession session = request.getSession();
      String url = "target/upload/" + filename + suffix;
      // 这个实在太慢了，所以另起一个线程进行操作
      Thread thread =
          new Thread(
              () -> {
                try {
                  dataManager.saveDataToDB(
                      dataManager.formatGData(url),
                      (String) session.getAttribute("username"),
                      description,
                      filename + suffix);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
      thread.start();
      // 记录日志
      logManager.log(
          new DataUploadLog((String) session.getAttribute("username"), filename + suffix));
      res.put("status", "success");
    } else {
      res.put("msg", "上传失败");
      res.put("status", "fail");
    }
    return res.toString();
  }

  @RequestMapping(value = "/getData", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getData() {
    List<DataBean> infos = dataManager.getData();
    if (infos.size() > 0) return JSON.toJSONString(infos);
    else return "{\"status\":\"fail\"}";
  }

  @RequestMapping(value = "/saveFileName", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String saveFileName(
      @RequestParam(value = "fileName") String fileName,
      @RequestParam(value = "userName") String userName,
      HttpServletRequest request) {
    HttpSession session = request.getSession();
    session.setAttribute("fileName", userName.split("：")[1] + "-" + fileName);
    // 记录日志
    logManager.log(new CheckDataLog((String) session.getAttribute("username"), fileName));
    return "{\"status\":\"success\"}";
  }

  @RequestMapping(value = "/getSingleData", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getSingleData(HttpServletRequest request) {
    String fileName = (String) request.getSession().getAttribute("fileName");
    List<DataGBean> results = dataManager.getSingleData(fileName);
    if (results.size() <= 0) {
      return "{\"status\":\"fail\"}";
    }
    return JSON.toJSONString(results);
  }

  @RequestMapping(value = "/getDataByFileName", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getDataByFileName(HttpServletRequest request) {
    String fileName = (String) request.getSession().getAttribute("fileName");
    DataBean dataBean = dataManager.getSimpleData(fileName);
    if (dataBean == null) {
      return "{\"status\":\"fail\"}";
    }
    return JSON.toJSONString(dataBean);
  }

  @RequestMapping(value = "/comment", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String comment(
      @RequestParam(value = "content") String content,
      @RequestParam(value = "fileName") String fileName,
      HttpServletRequest request) {
    HttpSession session = request.getSession();
    // 记录日志
    logManager.log(new CommentLog((String) session.getAttribute("username"), fileName, content));
    if (dataManager.saveContent(content, session.getAttribute("username"), fileName))
      return "{\"status\":\"success\",\"userName\":\"" + session.getAttribute("username") + "\"}";
    return "{\"status\":\"fail\"}";
  }

  @RequestMapping(value = "/getAllComment", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String getAllComment(@RequestParam(value = "fileName") String fileName) {
    List<MessageBean> results = dataManager.getAllComment(fileName);
    return JSON.toJSONString(results);
  }
}
