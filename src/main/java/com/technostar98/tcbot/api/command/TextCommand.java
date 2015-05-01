package com.technostar98.tcbot.api.command;

import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.List;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class TextCommand extends Command {
    private String message = null;
    private boolean usersAccepted = false;

    public TextCommand(String name, String server, UserLevel... uls){
        super(name, CommandType.USER_MESSAGE, server, uls);
    }

    public TextCommand(String name, String server, String message, UserLevel... uls){
        super(name, CommandType.USER_MESSAGE, server, uls);
        setMessage(message);
    }

    @Override
    public final String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        return this.message;
    }

    @Override
    public final String getHelpMessage() {
        return null;
    }

    @Override
    public final boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        return false;
    }

    @Override
    public final void close() {
        super.close();
    }

    public final void setMessage(String newMessage){
        this.message = newMessage;
    }

    public final void setRequiredUserLevels(List<UserLevel> uls){
        this.requiredULs = uls;
    }

    public final void setAcceptAllUsers(boolean acceptAllUsers){
        this.usersAccepted = acceptAllUsers;
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return usersAccepted || event.getUser().getUserLevels(event.getChannel()).stream().anyMatch(ul -> requiredULs.contains(ul));
    }
}
