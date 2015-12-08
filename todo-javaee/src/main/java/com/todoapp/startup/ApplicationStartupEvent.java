package com.todoapp.startup;

import com.todoapp.logging.Log;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class ApplicationStartupEvent {

  @Inject
  Log log;

  @PostConstruct
  public void init() {
    log.info("Application started.");
  }
}
