package com.graduation.logic.log;

import com.graduation.log.AbstractLog;
import com.graduation.log.LogServer;
import org.springframework.stereotype.Service;

/**
 * @author WuGYu
 * @date 2018/3/21 16:53
 */
@Service
public class LogManager {
  private LogServer logServer = new LogServer();

  public void log(AbstractLog log) {
    logServer.log(log);
  }
}
