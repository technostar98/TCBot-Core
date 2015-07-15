package api.filter.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubscribedEvent {
    public EventPriority priority() default EventPriority.NORMAL;
    public boolean canceled() default false;
}
