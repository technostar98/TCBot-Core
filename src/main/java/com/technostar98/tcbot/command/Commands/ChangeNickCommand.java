package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.Configuration;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import com.technostar98.tcbot.bot.BotManager;
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
public class ChangeNickCommand extends Command {

    public ChangeNickCommand(String server){
        super("nick", CommandType.GENERIC, server);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        runCommand(event);
        return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        String[] words = event.getEvent().getMessage().split(" ");
        if(words.length >= 2) {
            if (((Configuration<Boolean>) BotManager.getBot(this.getServer()).getConfiguration("nickChangeAllowed")).getValue()) {
                event.getEvent().getBot().sendIRC().changeNick(words[1]);
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public String getHelpMessage() {
        return "!nick newNick";
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return event.getUser().getNick().contains("Horf");//TODO proper superuser check
    }
}
