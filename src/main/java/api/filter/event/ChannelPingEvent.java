package api.filter.event;

import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class ChannelPingEvent extends GenericChatEvent {
    public ChannelPingEvent(EventContext context) {
        super(context);
    }

    public boolean isActionPing(){
        return context.event instanceof ActionEvent;
    }

    public boolean isMessagePing(){
        return context.event instanceof MessageEvent;
    }

    public ActionEvent getActionEvent(){
        return (ActionEvent)context.event;
    }

    public MessageEvent getMessageEvent(){
        return (MessageEvent)context.event;
    }
}
