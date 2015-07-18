package com.technostar98.tcbot.command.commands;

import api.command.Command;
import api.command.CommandType;
import api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ChangeServerCommand extends Command {

    public ChangeServerCommand(String server) {
        super("server", CommandType.GENERIC, server, "server", UserLevel.OP);
        //TODO Change server command
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event, Object... args) {
        return true;
    }

    @Override
    public String getHelpMessage() {
        return "!server newServer (WIP)";
    }
}
