package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

public class LeaveChannelCommand extends Command{

    public LeaveChannelCommand(String server){
        super("leave", CommandType.LEAVE, server, UserLevel.OP, UserLevel.OP);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        return "See ya later!";
    }

    @Override
    public void runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        return;
    }
}
