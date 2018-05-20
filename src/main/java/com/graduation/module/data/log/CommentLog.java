package com.graduation.module.data.log;

import com.graduation.log.UserLog;

/**
 * @author WuGYu
 * @date 2018/5/20 17:38
 */
public class CommentLog extends UserLog {
  private String fileName;
  private String content;

  public CommentLog(String userName, String fileName, String content) {
    super(userName);
    this.fileName = fileName;
    this.content = content;
  }
}
