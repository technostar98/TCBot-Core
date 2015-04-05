package com.technostar.tcbot.command.Commands;

import com.technostar.tcbot.command.Command;
import com.technostar.tcbot.command.CommandType;
import com.technostar.tcbot.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Created by bret on 4/3/15.
 */
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
