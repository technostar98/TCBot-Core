package api.filter.event;

import org.pircbotx.hooks.events.ActionEvent;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class ChannelActionEvent extends Event{
    public ChannelActionEvent(EventContext context) {
        super(context);
    }

    public ActionEvent getEvent(){
        return (ActionEvent)context.event;
    }
}
