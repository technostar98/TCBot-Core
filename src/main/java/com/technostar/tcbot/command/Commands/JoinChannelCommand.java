package com.technostar.tcbot.command.Commands;

import com.technostar.tcbot.command.Command;
import com.technostar.tcbot.command.CommandType;
import com.technostar.tcbot.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Created by bret on 4/3/15.
 */
public class JoinChannelCommand extends Command{

    public JoinChannelCommand(String server){
        super("join", CommandType.JOIN, server);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        String message = event.getEvent().getMessage();
        if(message.indexOf(" ") < 0) return null;
        else{
            int spaceIndex = message.indexOf(" ");
            if(spaceIndex == message.length() - 1) return null;
            else{
                String partedMessage = message.substring(++spaceIndex);
                spaceIndex = partedMessage.indexOf(" ");
                if(spaceIndex == 0) return null;
                else if(spaceIndex < 0) return partedMessage;
                else return partedMessage.substring(0, spaceIndex);
            }
        }
    }

    @Override
    public void runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {

    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return event.getUser().getRealName().equals("Horf") || event.getUser().getNick().contains("Techno");
    }
}
