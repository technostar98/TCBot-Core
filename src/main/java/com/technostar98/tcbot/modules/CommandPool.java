package com.technostar98.tcbot.modules;

import api.command.Command;
import api.command.ICommandManager;
import api.filter.ChatFilter;
import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.technostar98.tcbot.command.Commands.*;
import com.technostar98.tcbot.command.Filters.TestFilter;

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

    //IDs
    private BiMap<String, Integer> botCommandIDs = HashBiMap.create();//String ID -- int ID; Module command string ids are prefaced by their module ID + ':', int ID unaffected
    private BiMap<String, Integer> botFilterIDS = HashBiMap.create();//String ID -- int ID; Module filter string ids are prefaces by their module ID + ':', int ID unaffected

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

        ChatFilter testFilter = new TestFilter(null, null);

        registerCommand(quit);
        registerCommand(join);
        registerCommand(leaveC);
        registerCommand(shutdown);
        registerCommand(filterToggle);
        registerCommand(help);
        registerCommand(nickChange);
        registerCommand(module);
        registerCommand(textControl);
        registerCommand(channel);

        registerFilter(testFilter);
    }


    @Override
    public Optional<Command> getCommand(String name) {
        return Optional.fromNullable(botCommands.get(botCommandIDs.get(name)));
    }

    @Override
    public Map<String, Command> getCommandsS() {
        return botCommands.keySet().stream().collect(Collectors.toMap(
                k -> botCommandIDs.inverse().get(k),
                v -> botCommands.get(v)
        ));
    }

    @Override
    public Map<Integer, Command> getCommands() {
        return botCommands;
    }

    @Override
    public Optional<ChatFilter> getFilter(String name) {
        return Optional.fromNullable(botFilters.get(botFilterIDS.get(name)));
    }

    @Override
    public Map<String, ChatFilter> getFiltersS() {
        return botFilters.keySet().stream().collect(Collectors.toMap(
                k -> botFilterIDS.inverse().get(k),
                v -> botFilters.get(v)
        ));
    }

    @Override
    public Map<Integer, ChatFilter> getFilters() {
        return botFilters;
    }

    @Override
    public Optional<Module> getModule(String name) {
        return Optional.fromNullable(moduleManager.getModule(name));
    }

    @Override
    public Map<String, Module> getModulesS() {
        return null;
    }

    @Override
    public Map<Integer, Module> getModules() {
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
    public void registerCommand(Command command) throws IllegalArgumentException {

    }

    @Override
    public void registerFilter(ChatFilter filter) {

    }

    @Override
    public Optional<Integer> getFilterID(String id) {
        return null;
    }

    @Override
    public Optional<Integer> getCommandID(String id) {
        return null;
    }

    @Override
    public Optional<Integer> getModuleID(String id) {
        return null;
    }

    @Override
    public Optional<String> getFilterSID(int id) {
        return null;
    }

    @Override
    public Optional<String> getCommandSID(int id) {
        return null;
    }

    @Override
    public Optional<String> getModuleSID(int id) {
        return null;
    }

    @Override
    public boolean loadModule(String id, String server, String channel) {
        return false;
    }

    @Override
    public Optional<String> getModuleAlias(String id) {
        return null;
    }

    @Override
    public Optional<String> getModuleSID(String alias) {
        return null;
    }

    @Override
    public void unregisterCommand(String id) {

    }

    @Override
    public void unregisterFilter(String id) {

    }

    @Override
    public void unregisterModule(String id, String server, String channel) {

    }

    @Override
    public void unregisterCommand(int id) {

    }

    @Override
    public void unregisterFilter(int id) {

    }

    @Override
    public void unregisterModule(int id, String server, String channel) {

    }

    @Override
    public Optional<Command> getModuleCommand(String module, String id) {
        return null;
    }

    @Override
    public Optional<Command> getModuleCommand(String module, int id) {
        return null;
    }

    @Override
    public Optional<Map<String, Command>> getModuleCommands(String moduleID) {
        return null;
    }

    @Override
    public Optional<ChatFilter> getModuleFilter(String module, String id) {
        return null;
    }

    @Override
    public Optional<ChatFilter> getModuleFilter(String module, int id) {
        return null;
    }

    @Override
    public Optional<Map<String, ChatFilter>> getModuleFilters(String moduleID) {
        return null;
    }

    @Override
    public boolean doesModuleCommandExist(String module, String id) {
        return false;
    }

    @Override
    public boolean doesModuleFilterExist(String module, String id) {
        return false;
    }

    @Override
    public void registerModuleCommand(String module, Command command) {

    }

    @Override
    public void registerModuleFilter(String module, ChatFilter filter) {

    }

    @Override
    public void unregisterModuleCommand(String module, String name) {

    }

    @Override
    public void unregisterModuleFilter(String module, String name) {

    }

    @Override
    public void refreshModule(String moduleID) {

    }
}
