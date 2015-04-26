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
        boolean ran = runCommand(event);
        if(ran) return "Shutting down....";
        else return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
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
        return event.getUser().getRealName().equals("Horf");
    }

    @Override
    public String getHelpMessage() {
        return "!shutdown (Only certain users can run this)";
    }
}
