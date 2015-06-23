package com.technostar98.tcbot.modules;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import api.command.Command;
import api.command.ICommandManager;
import api.filter.ChatFilter;
import com.technostar98.tcbot.command.Commands.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
public enum CommandPool implements ICommandManager {
    INSTANCE;

    private Map<String, Command> botCommands = new ConcurrentHashMap<>();
    private Map<String, ChatFilter> botFilters = new ConcurrentHashMap<>();
    private Map<String, List<Command>> moduleCommands = new HashMap<>();
    private Map<String, List<ChatFilter>> moduleFilters = new HashMap<>();
    private ModuleManager moduleManager = new ModuleManager();
    private Table<String, String, List<String>> channelLoadedModules = TreeBasedTable.create(
            (s1, s2) -> s1.compareTo(s2),
            (s1, s2) -> s1.compareTo(s2)
    );

    CommandPool(){
        Command quit = new BotQuitCommand(null);
        Command join = new JoinChannelCommand(null);
        Command leaveC = new LeaveChannelCommand(null);
        Command shutdown = new ShutdownCommand(null);
        Command filterToggle = new FilterToggleCommand(null);
        Command help = new HelpCommand(null);
        Command nickChange = new ChangeNickCommand(null);
        Command module = new ModuleControllerCommand(null);
        Command textControl = new TextCommandControllerCommand(null);
        Command channel = new ChannelCommand(null);

        addCommand(quit);
        addCommand(join);
        addCommand(leaveC);
        addCommand(shutdown);
        addCommand(filterToggle);
        addCommand(help);
        addCommand(nickChange);
        addCommand(module);
        addCommand(textControl);
        addCommand(channel);
    }

    @Override
    public Command getCommand(String name) {
        return botCommands.get(name);
    }

    @Override
    public List<Command> getCommands() {
        return botCommands.isEmpty() ? null : botCommands.keySet().stream().map(s -> getCommand(s)).collect(Collectors.toList());
    }

    @Override
    public ChatFilter getFilter(String name) {
        return botFilters.get(name);
    }

    @Override
    public List<ChatFilter> getFilters() {
        return botFilters.isEmpty() ? null : botFilters.keySet().stream().map(s -> getFilter(s)).collect(Collectors.toList());
    }

    @Override
    public Module getModule(String name) {
        return moduleManager.getModule(name);
    }

    @Override
    public List<Module> getModules() {
        return moduleManager.getModuleNames().isEmpty() ? null : moduleManager.getModules();
    }

    @Override
    public boolean doesCommandExist(String name) {
        return botCommands.containsKey(name);
    }

    @Override
    public boolean doesFilterExist(String name) {
        return botFilters.containsKey(name);
    }

    @Override
    public boolean doesModuleExist(String name) {
        return moduleManager.isModuleLoaded(name);
    }

    @Override
    public void addCommand(String name, Command command) {
        botCommands.put(name, command);
    }

    @Override
    public void addCommand(Command command) {
        botCommands.put(command.getName(), command);
    }

    @Override
    public void addFilter(String name, ChatFilter filter) {
        botFilters.put(name, filter);
    }

    @Override
    public void addFilter(ChatFilter filter) {
        botFilters.put(filter.getName(), filter);
    }

    @Override
    public boolean loadModule(String name, String server, String channel) {//When the module is loaded, the module should add all the commands and filters on its own
        boolean worked = moduleManager.loadModule(name);
        if(worked) {
            List<String> loadedModules = channelLoadedModules.get(server, channel);
            if (loadedModules == null) {
                loadedModules = new LinkedList<>();
            }

            loadedModules.add(name);
            channelLoadedModules.put(server, channel, loadedModules);
        }

        return worked;
    }

    @Override
    public void removeCommand(String name) {
        botCommands.remove(name);
    }

    @Override
    public void removeFilter(String name) {
        botFilters.remove(name);
    }

    @Override
    public void removeModule(String name, String server, String channel) {
        Module m = moduleManager.getModule(name);
        if(m != null){
            m.getCommands().forEach(n -> removeModuleCommand(name, n));
            m.getFilters().forEach(n -> removeModuleFilter(name, n));
        }

        moduleManager.unloadModule(name);

        List<String> modulesLoaded = channelLoadedModules.get(server, channel);
        modulesLoaded.remove(name);
        channelLoadedModules.put(server, channel, modulesLoaded);

    }

    @Override
    public Command getModuleCommand(String module, String name) {
        return moduleCommands.get(module).get(moduleCommands.get(module).indexOf(name));
    }

    @Override
    public List<Command> getModuleCommands(String module) {
        return moduleCommands.get(module);
    }

    @Override
    public ChatFilter getModuleFilter(String module, String name) {
        return moduleFilters.get(module).get(moduleFilters.get(module).indexOf(name));
    }

    @Override
    public List<ChatFilter> getModuleFilters(String module) {
        return moduleFilters.get(module);
    }

    @Override
    public boolean doesModuleCommandExist(String module, String name) {
        return module.equals("*") ? moduleCommands.keySet().parallelStream().anyMatch(s -> moduleCommands.get(s).parallelStream().anyMatch(c -> c.getName().equals(name))) : moduleCommands.get(module).stream().anyMatch(c -> c.getName().equals(name));
    }

    @Override
    public boolean doesModuleFilterExist(String module, String name) {
        return module.equals("*") ? moduleFilters.keySet().parallelStream().anyMatch(s -> moduleFilters.get(s).parallelStream().anyMatch(f -> f.getName().equals(name))) : moduleFilters.get(module).stream().anyMatch(f -> f.getName().equals(name));
    }

    @Override
    public void addModuleCommand(String module, Command command) {
        moduleCommands.get(module).add(command);
    }

    @Override
    public void addModuleFilter(String module, ChatFilter filter) {
        moduleFilters.get(module).add(filter);
    }

    @Override
    public void removeModuleCommand(String module, String name) {
        moduleCommands.get(module).remove(getModuleCommand(module, name));
    }

    @Override
    public void removeModuleFilter(String module, String name) {
        moduleFilters.get(module).remove(getModuleFilter(module, name));
    }

    private void checkModuleLoadStatus(){
        //TODO unload unused modules
    }
}
