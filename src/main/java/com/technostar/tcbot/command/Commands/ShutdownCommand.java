package com.technostar.tcbot.command.Commands;

import com.technostar.tcbot.bot.BotManager;
import com.technostar.tcbot.command.Command;
import com.technostar.tcbot.command.CommandType;
import com.technostar.tcbot.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bret on 4/3/15.
 */
public class ShutdownCommand extends Command {

    public ShutdownCommand(String server){
        super("shutdown", CommandType.LEAVE, server);
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
                    BotManager.stop();
                }
            }, 500L);
        }
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return event.getUser().getRealName().equals("Horf");
    }
}
