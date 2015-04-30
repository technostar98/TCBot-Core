package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
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
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        return true;
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return event.getUser().getRealName().equals("Horf") || event.getUser().getNick().contains("Techno");
    }

    @Override
    public String getHelpMessage() {
        return "!join target (only certain users can use this)";
    }
}
