package api.filter.event;

import org.pircbotx.hooks.events.KickEvent;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class UserKickedEvent extends Event{
    public UserKickedEvent(EventContext context) {
        super(context);
    }

    public KickEvent getEvent(){
        return (KickEvent)context.event;
    }
}
