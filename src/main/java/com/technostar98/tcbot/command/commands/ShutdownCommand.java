package com.technostar98.tcbot.command.commands;

import com.technostar98.tcbot.bot.BotManager;
import api.command.Command;
import api.command.CommandType;
import api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ShutdownCommand extends Command {

    public ShutdownCommand(String server){
        super("shutdown", CommandType.USER_MESSAGE, server, "shutdown");
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        boolean ran = runCommand(event);
        if(ran) return "Shutting down....";
        else return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event, Object... args) {
        if(isUserAllowed(event.getEvent())){
            Timer timer = new Timer("Shutdown");
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    BotManager.stopServer(getServer());
                }
            }, 1000L);
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return BotManager.getBot(getServer()).getServerConfiguration().getSuperusers().contains(event.getUser().getNick());
    }

    @Override
    public String getHelpMessage() {
        return "!shutdown (Only certain users can run this)";
    }
}
