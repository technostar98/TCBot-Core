package api.filter.event;

import org.pircbotx.hooks.events.JoinEvent;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class UserJoinEvent extends Event {
    public UserJoinEvent(EventContext context) {
        super(context);
    }

    public JoinEvent getEvent(){
        return (JoinEvent)context.event;
    }
}
