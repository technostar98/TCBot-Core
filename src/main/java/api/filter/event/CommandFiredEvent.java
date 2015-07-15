package api.filter.event;

import api.command.Command;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class CommandFiredEvent extends Event {
    public final Command command;

    public CommandFiredEvent(EventContext context, Command command) {
        super(context);
        this.command = command;
    }

    public MessageEvent getEvent(){
        return (MessageEvent)context.event;
    }
}
