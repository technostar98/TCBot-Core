package com.technostar98.tcbot.command.commands;

import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.bot.BotState;
import api.command.Command;
import api.command.CommandType;
import api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
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
public class BotQuitCommand extends Command {

    public BotQuitCommand(String server){
        super("quit", CommandType.LEAVE, server, "quit", UserLevel.OP, UserLevel.OWNER);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        runCommand(event);
        return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event, Object... args) {
        boolean allowed = isUserAllowed(event.getEvent());
        if(allowed){
            event.getEvent().getBot().sendIRC().quitServer("Cya later, alligators!");
            BotManager.getBot(getServer()).setState(BotState.STOPPED);
        }

        return allowed;
    }

    @Override
    public String getHelpMessage() {
        return "!quit (WIP)";
    }
}
