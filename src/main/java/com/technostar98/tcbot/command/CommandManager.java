package com.technostar98.tcbot.command;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.filter.ChatFilter;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to hold modules and commands enabled for a bot/channel/whatever
 */
public class CommandManager {
    //TODO add modules support
    private ArrayList<Command> commands = new ArrayList<>();
    private ArrayList<ChatFilter> filters = new ArrayList<>();
    private final String channel, server;

    public CommandManager(String server, String channel){
        this.channel = channel;
        this.server = server;
    }

    public List<Command> getCommands(String name){
        List<Command> matched = commands.parallelStream().filter(c -> c.getName().equals(name)).collect(Collectors.toList());
        if(!matched.isEmpty()) return matched;
        else return null;
    }

    public List<Command> getCommands(){
        return this.commands.isEmpty() ? null : this.commands;
    }

    public List<ChatFilter> getFilters(String name){
        List<ChatFilter> matched = filters.parallelStream().filter(f -> f.getName().equals(name)).collect(Collectors.toList());
        if (!matched.isEmpty()) return matched;
        else return null;
    }

    public List<ChatFilter> getFilters(){
        return this.filters.isEmpty() ? null : this.filters;
    }

    public Command getCommand(String name){
        List<Command> commands = getCommands(name);
        if(commands != null && !commands.isEmpty()) return commands.get(0);
        else return null;
    }

    public ChatFilter getFilter(String name){
        List<ChatFilter> filters = getFilters(name);
        if(filters != null && !filters.isEmpty()) return filters.get(0);
        else return null;
    }

    public String enactCommand(CommandType type, String name, WrappedEvent<MessageEvent<PircBotX>> event){
//        System.out.println("COMMAND: " + name + "\tUSER: " + event.getEvent().getUser().getNick());

        List<Command> matched = commands.parallelStream().filter(c -> /*c.getCommandType() == type &&*/
                (c.getName().equals(name) || c.getAlias().equals(name))).collect(Collectors.toList());
        if(!matched.isEmpty()) return matched.get(0).getMessage(event);
        else return null;
    }

    public void addCommand(Command command){
        if(!commands.contains(command)) commands.add(command);
    }

    public void addFilter(ChatFilter filter){
        if(!this.filters.contains(filter))
            this.filters.add(filter);
    }

    public String getChannel() {
        return channel;
    }

    public String getServer() {
        return server;
    }
}
