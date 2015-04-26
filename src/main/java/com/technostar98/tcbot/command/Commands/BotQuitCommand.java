package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.bot.BotState;
import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

public class BotQuitCommand extends Command {

    public BotQuitCommand(String server){
        super("quit", CommandType.LEAVE, server, UserLevel.OP, UserLevel.OWNER);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        runCommand(event);
        return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
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
