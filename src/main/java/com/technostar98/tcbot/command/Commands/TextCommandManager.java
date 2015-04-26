package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;


public class TextCommandManager extends Command{

    public TextCommandManager(String server) {
        super("command", CommandType.USER_MESSAGE, server, UserLevel.OP, UserLevel.OWNER);
        //TODO Text commands
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        return true;
    }

    @Override
    public String getHelpMessage() {
        return "!addCommand <--ul={user|op}> commandText (type !help textVariables for more info)";
    }
}
