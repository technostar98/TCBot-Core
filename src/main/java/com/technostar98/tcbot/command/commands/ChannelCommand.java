package com.technostar98.tcbot.command.commands;

import api.command.Command;
import api.command.CommandType;
import api.lib.WrappedEvent;
import com.technostar98.tcbot.bot.BotManager;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * All terms and conditions of using said code
 * are determined by the GNU LGPL 3.0.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ChannelCommand extends Command {

    public ChannelCommand(String server){
        super("channel", CommandType.USER_MESSAGE, server, "channel", UserLevel.OP, UserLevel.OWNER);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        return runCommand(event) ? "Successfully updated channel settings" : "Failed to update channel settings";
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event, Object... args) {
        String[] words = event.getEvent().getMessage().split(" ");

        if(words.length >= 3){
            String action = words[1];
            String arg = words[2];

            if(action.equals("autojoin")){
                if(arg.equals("on")){
                    BotManager.getBot(getServer()).getServerConfiguration().addChannel(event.getEvent().getChannel().getName());
                }else if(arg.equals("off")){
                    BotManager.getBot(getServer()).getServerConfiguration().removeChannel(event.getEvent().getChannel().getName());
                }else{
                    return false;
                }

                BotManager.saveServerConfigurations();
                return  true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    public String getHelpMessage() {
        return "!channel {autojoin} {on|off}";
    }
}
