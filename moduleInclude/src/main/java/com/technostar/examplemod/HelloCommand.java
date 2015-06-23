package com.technostar.examplemod;

import api.command.Command;
import api.command.CommandType;
import api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

public class HelloCommand extends Command{

    public HelloCommand(String server){
        super("hello", CommandType.USER_MESSAGE, server);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        return "Hello " + event.getEvent().getUser().getNick() + "!";
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        return false;
    }

    @Override
    public String getHelpMessage() {
        return "!hello";
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return true;
    }
}
