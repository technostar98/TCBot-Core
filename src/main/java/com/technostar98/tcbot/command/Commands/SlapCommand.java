package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Created by bret on 4/3/15.
 */
public class SlapCommand extends Command{

    public SlapCommand(String server){
        super("slap", CommandType.ACTION, server);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        String message = event.getEvent().getMessage();
        String target;
        if(message.indexOf(" ") < 0) target = event.getEvent().getUser().getNick();
        else{
            int spaceIndex = message.indexOf(" ");
            if(spaceIndex == message.length() - 1) target = event.getEvent().getUser().getNick();
            else{
                String partedMessage = message.substring(++spaceIndex);
                spaceIndex = partedMessage.indexOf(" ");
                if(spaceIndex == 0) target = event.getEvent().getUser().getNick();
                else if(spaceIndex < 0) target = partedMessage;
                else target = partedMessage.substring(0, spaceIndex);
            }
        }
        return "slaps " + target + " around a bit with a large trout";
    }

    @Override
    public void runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        return;
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return true;
    }
}
