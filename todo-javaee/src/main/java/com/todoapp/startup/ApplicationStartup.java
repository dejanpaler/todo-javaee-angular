package com.todoapp.startup;

import com.todoapp.logging.Log;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Singleton
@Startup
public class ApplicationStartup {

  @Inject
  Log log;

  @Inject
  Event<ApplicationStartupEvent> event;

  @Inject
  ApplicationStartupEvent applicationStartupEvent;

  /**
   * Fires start up event when application is started.
   */
  @PostConstruct
  public void init() {
    log.info("Firing application startup event...");
    event.fire(applicationStartupEvent);
  }
}
