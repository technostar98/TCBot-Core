package com.technostar98.tcbot.api.command;

import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Any hard-coded commands should extend this class
 */
public abstract class Command {
    public final String name;
    public String alias; //Allows for the channel to rename a command
    public List<UserLevel> requiredULs;
    public final CommandType commandType;
    private String server;

    public Command(String name, CommandType type, String server, UserLevel... uls){
        this.name = name;
        this.alias = name;
        this.requiredULs = Arrays.asList(uls);
        this.commandType = type;
    }

    public String getAlias() {
        return alias;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public List<UserLevel> getRequiredULs() {
        return requiredULs;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public boolean isUserAllowed(MessageEvent<PircBotX> event){
        return requiredULs != null && !requiredULs.isEmpty()
                && event.getUser().getUserLevels(event.getChannel()).stream().anyMatch(ul -> requiredULs.contains(ul));
    }

    public void close(){
        return;
    }

    public abstract String getMessage(WrappedEvent<MessageEvent<PircBotX>> event);
    public abstract boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event);
    public abstract String getHelpMessage();

    @Override
    public String toString() {
        return "[" + name + ", " + alias + "]";
    }
}
