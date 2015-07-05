package si.todoapp.startup;

import si.todoapp.logging.Log;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Singleton
@Startup
public class StartupEventNotifier {

    @Inject
    Log log;

    @Inject
    Event<StartupEvent> event;

    @PostConstruct
    public void init() {
        log.info("Application started.");
        event.fire(new StartupEvent(){
        });
    }
}