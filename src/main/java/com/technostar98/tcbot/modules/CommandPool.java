package com.technostar98.tcbot.modules;

import api.command.Command;
import api.command.ICommandManager;
import api.filter.ChatFilter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.technostar98.tcbot.command.Commands.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    //IDs
    private BiMap<String, Integer> botCommandIDs = HashBiMap.create();//String id -- int id; Module command string ids are prefaced by their module id + ':', int id unaffected
    private BiMap<String, Integer> botFilterIDS = HashBiMap.create();//String id -- int id; Module filter string ids are prefaces by their module id + ':', int id unaffected
    private BiMap<String, Integer> moduleIDS = HashBiMap.create();//String module id -- internalized id

    private Map<Integer, Command> botCommands = new ConcurrentHashMap<>();
    private Map<Integer, ChatFilter> botFilters = new ConcurrentHashMap<>();
    private ModuleManager moduleManager = new ModuleManager();
    private Table<String, String, List<String>> channelLoadedModules = TreeBasedTable.create(
            (s1, s2) -> s1.compareTo(s2),
            (s1, s2) -> s1.compareTo(s2)
    );

    CommandPool(){
        //Core commands
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
        return null;
    }

    @Override
    public Map<String, Command> getCommands() {
        return null;
    }

    @Override
    public ChatFilter getFilter(String name) {
        return null;
    }

    @Override
    public List<String> getFilters() {
        return null;
    }

    @Override
    public Module getModule(String name) {
        return null;
    }

    @Override
    public Map<String, Module> getModules() {
        return null;
    }

    @Override
    public boolean doesCommandExist(String name) {
        return false;
    }

    @Override
    public boolean doesFilterExist(String name) {
        return false;
    }

    @Override
    public boolean doesModuleExist(String name) {
        return false;
    }

    @Override
    public void addCommand(String name, Command command) {

    }

    @Override
    public void addCommand(Command command) {

    }

    @Override
    public void addFilter(String name, ChatFilter filter) {

    }

    @Override
    public void addFilter(ChatFilter filter) {

    }

    @Override
    public boolean loadModule(String name, String server, String channel) {
        return false;
    }

    @Override
    public void removeCommand(String name) {

    }

    @Override
    public void removeFilter(String name) {

    }

    @Override
    public void removeModule(String name, String server, String channel) {

    }

    @Override
    public Command getModuleCommand(String module, String name) {
        return null;
    }

    @Override
    public Map<String, Command> getModuleCommands(String module) {
        return null;
    }

    @Override
    public ChatFilter getModuleFilter(String module, String name) {
        return null;
    }

    @Override
    public Map<String, ChatFilter> getModuleFilters(String module) {
        return null;
    }

    @Override
    public boolean doesModuleCommandExist(String module, String name) {
        return false;
    }

    @Override
    public boolean doesModuleFilterExist(String module, String name) {
        return false;
    }

    @Override
    public void addModuleCommand(String module, Command command) {

    }

    @Override
    public void addModuleFilter(String module, ChatFilter filter) {

    }

    @Override
    public void removeModuleCommand(String module, String name) {

    }

    @Override
    public void removeModuleFilter(String module, String name) {

    }
}
