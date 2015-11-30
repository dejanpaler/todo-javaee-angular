package com.todoapp.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DelegatingLog implements Log {

  private final Logger logger;

  public DelegatingLog(final Logger logger) {
    this.logger = logger;
  }

  @Override
  public void info(String msg) {
    logger.log(Level.INFO, msg);
  }

  @Override
  public void info(String msg, Object param) {
    logger.log(Level.INFO, msg, param);
  }

  @Override
  public void info(String msg, Object... params) {
    logger.log(Level.INFO, msg, params);
  }

}
