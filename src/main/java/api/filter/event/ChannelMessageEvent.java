package api.filter.event;

import org.pircbotx.hooks.events.MessageEvent;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class ChannelMessageEvent extends GenericChatEvent {
    public ChannelMessageEvent(EventContext context) {
        super(context);
    }

    public MessageEvent getEvent(){
        return (MessageEvent)context.event;
    }
}
