package com.todoapp.logging;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class LogProducer {

  /**
   * Produces the application logger.
   *
   * @param injectionPoint - injection point of the logger
   * @return - the logger for the application
   */
  @Produces
  public Log getLogger(InjectionPoint injectionPoint) {
    Class<?> declaringClass = injectionPoint.getMember().getDeclaringClass();
    Logger logger = Logger.getLogger(declaringClass.getName());
    return new DelegatingLog(logger);
  }

}
