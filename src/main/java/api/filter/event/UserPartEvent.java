package api.filter.event;

import org.pircbotx.hooks.events.PartEvent;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class UserPartEvent extends Event {
    public UserPartEvent(EventContext context) {
        super(context);
    }

    public PartEvent getEvent(){
        return (PartEvent)context.event;
    }
}
