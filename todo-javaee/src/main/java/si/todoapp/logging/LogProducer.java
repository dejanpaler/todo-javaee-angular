package si.todoapp.logging;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

public class LogProducer {

    @Produces
    public Log getLogger(InjectionPoint injectionPoint) {
        Class<?> declaringClass = injectionPoint.getMember().getDeclaringClass();
        Logger logger = Logger.getLogger(declaringClass.getName());
        return new DelegatingLog(logger);
    }

}
