package si.todoapp.startup;

import org.junit.Test;
import si.todoapp.logging.Log;

import javax.enterprise.event.Event;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StartupEventNotifierTest {

    private Log log = mock(Log.class);

    @SuppressWarnings("unchecked")
    private Event<StartupEvent> event = mock(Event.class);

    @Test
    public void shouldFireStartupEvent() throws Exception {
        StartupEventNotifier startupEventNotifier = new StartupEventNotifier();
        startupEventNotifier.log = log;
        startupEventNotifier.event = event;

        startupEventNotifier.init();

        verify(log).info("Application started.");
        verify(event).fire(any(StartupEvent.class));
    }
}
