package com.technostar.tcbot.command.Commands;

import com.technostar.tcbot.bot.BotManager;
import com.technostar.tcbot.bot.BotState;
import com.technostar.tcbot.command.Command;
import com.technostar.tcbot.command.CommandType;
import com.technostar.tcbot.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Created by bret on 4/3/15.
 */
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
    public void runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        boolean allowed = isUserAllowed(event.getEvent());
        if(allowed){
            event.getEvent().getBot().sendIRC().quitServer("Cya later, alligators!");
            BotManager.getBot(server).setState(BotState.STOPPED);
        }
    }
}
