package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Timer;
import java.util.TimerTask;

public class ShutdownCommand extends Command {

    public ShutdownCommand(String server){
        super("shutdown", CommandType.USER_MESSAGE, server);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        runCommand(event);
        if(isUserAllowed(event.getEvent())) return "Shutting down....";
        else return null;
    }

    @Override
    public void runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        if(isUserAllowed(event.getEvent())){
            Timer timer = new Timer("Shutdown");
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    BotManager.stopServer(getServer());
                }
            }, 1000L);
        }
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return event.getUser().getRealName().equals("Horf");
    }
}
