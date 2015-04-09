package com.technostar98.tcbot.modules;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.filter.ChatFilter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by bret on 4/8/15.
 */
public class Module {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final HashMap<String, ChatFilter> filters = new HashMap<>();
    private final String name, ID;
    private final int version;

    public Module(String name, String id, int version, List<Command> commandList, List<ChatFilter> filterList){
        this.name = name;
        this.ID = id;
        this.version = version;
        commandList.parallelStream().sorted((a, b) -> a.getName().compareTo(b.getName())).forEach(a -> commands.put(a.getName(), a));
        filterList.parallelStream().sorted((a, b) -> a.getName().compareTo(b.getName())).forEach(a -> filters.put(a.getName(), a));
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public int getVersion() {
        return version;
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public HashMap<String, ChatFilter> getFilters() {
        return filters;
    }

    public Command getCommand(String name){
        return commands.get(name);
    }

    public ChatFilter getFilter(String name){
        return filters.get(name);
    }
}
