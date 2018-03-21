package com.graduation.log;

/**
 * @author WuGYu
 * @date 2018/3/21 15:32
 */
public abstract class AbstractLog {
  private final long time;

  public AbstractLog() {
    this.time = System.currentTimeMillis();
  }
}
