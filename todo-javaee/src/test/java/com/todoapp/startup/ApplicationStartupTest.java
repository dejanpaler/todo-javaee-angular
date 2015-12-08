package com.todoapp.startup;

import com.todoapp.logging.Log;

import org.junit.Test;

import javax.enterprise.event.Event;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ApplicationStartupTest {

  private Log log = mock(Log.class);

  @SuppressWarnings("unchecked")
  private Event<ApplicationStartupEvent> event = mock(Event.class);

  @Test
  public void shouldFireStartupEvent() throws Exception {
    ApplicationStartup applicationStartup = new ApplicationStartup();
    applicationStartup.log = log;
    applicationStartup.event = event;

    applicationStartup.init();

    verify(log).info("Firing application startup event...");
    verify(event).fire(any(ApplicationStartupEvent.class));
  }
}
