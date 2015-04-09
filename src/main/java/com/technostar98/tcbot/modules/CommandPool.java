package com.technostar98.tcbot.modules;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.filter.ChatFilter;
import com.technostar98.tcbot.command.Commands.*;
import com.technostar98.tcbot.command.Filters.DevinSlapFilter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bret on 4/7/15.
 */
public class CommandPool {

    private static HashMap<String, Command> botCommands = new HashMap<>();
    private static HashMap<String, ChatFilter> botFilters = new HashMap<>();
    private static HashMap<String, Module> modules = new HashMap<>();

    static {
        Command quit = new BotQuitCommand(null);
        Command join = new JoinChannelCommand(null);
        Command leaveC = new LeaveChannelCommand(null);
        Command shutdown = new ShutdownCommand(null);

        botCommands.put(quit.getName(), quit);
        botCommands.put(join.getName(), join);
        botCommands.put(leaveC.getName(), leaveC);
        botCommands.put(shutdown.getName(), shutdown);

        ChatFilter devinSlap = new DevinSlapFilter(null);

        botFilters.put(devinSlap.getName(), devinSlap);
    }

    public static boolean loadModule(String module){
        //TODO load modules
        return false;
    }

    public static List<Command> getBotCommandsList(){
        return botCommands.keySet().stream().map(s -> botCommands.get(s)).collect(Collectors.toList());
    }

    public static List<ChatFilter> getBotFilterList(){
        return botFilters.keySet().stream().map(s -> botFilters.get(s)).collect(Collectors.toList());
    }

    public static List<Module> getModules(){
        return modules.keySet().stream().map(m -> modules.get(m)).collect(Collectors.toList());
    }

    public static Module getModule(String name){
        return modules.get(name);
    }

    public static List<Command> getModuleCommands(String name){
        Module m = getModule(name);
        return m.getCommands().keySet().stream().map(n -> m.getCommand(n)).collect(Collectors.toList());
    }

    public static List<ChatFilter> getModuleFilters(String name){
        Module m = getModule(name);
        return m.getFilters().keySet().stream().map(n -> m.getFilter(n)).collect(Collectors.toList());
    }
}
