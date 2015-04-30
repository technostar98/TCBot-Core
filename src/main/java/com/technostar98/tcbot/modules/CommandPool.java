package com.technostar98.tcbot.modules;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.filter.ChatFilter;
import com.technostar98.tcbot.command.Commands.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
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
        Command filterToggle = new FilterToggleCommand(null);
        Command help = new HelpCommand(null);
        Command nickChange = new ChangeNickCommand(null);
        Command module = new ModuleControllerCommand(null);

        botCommands.put(quit.getName(), quit);
        botCommands.put(join.getName(), join);
        botCommands.put(leaveC.getName(), leaveC);
        botCommands.put(shutdown.getName(), shutdown);
        botCommands.put(filterToggle.getName(), filterToggle);
        botCommands.put(help.getName(), help);
        botCommands.put(nickChange.getName(), nickChange);
        botCommands.put(module.getName(), module);
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

    public static boolean doesFilterExist(String name, String module){
        boolean exists;

        if(module != null){
            if(CommandPool.getModule(module) != null)
                exists = CommandPool.getModule(module).getFilter(name) != null;
            else
                exists = false;
        }else{
            exists = CommandPool.getBotFilterList().stream().anyMatch(f -> f.getName().equals(name));
        }

        return exists;
    }
}
