package com.technostar98.tcbot.modules;

import api.command.Command;
import api.command.ICommandFilterRegistry;
import api.filter.ChatFilter;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import com.technostar98.tcbot.command.Filters.TestFilter;
import com.technostar98.tcbot.command.commands.*;
import com.technostar98.tcbot.lib.Logger;

import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
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
public enum CommandPool implements ICommandFilterRegistry {
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

    private int lastFilterID = -1, lastCommandID = -1;
    private final int maxIDS = 16384;//Arbitrarily large number for looping and finding ids

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
        return moduleManager.getModule(name);
    }

    @Override
    public Map<String, Module> getModulesS() {
        return moduleManager.idsToNames.keySet().stream().collect(Collectors.toMap(s -> s, s -> moduleManager.getModule(s).get()));
    }

    @Override
    public boolean doesCommandExist(String name) {
        return botCommands.containsKey(botCommandIDs.get(name));
    }

    @Override
    public boolean doesFilterExist(String name) {
        return botFilters.containsKey(botFilterIDS.get(name));
    }

    @Override
    public boolean doesModuleExist(String name) {
        return moduleManager.getModule(name).isPresent();
    }

    @Override
    public void registerCommand(Command command) throws IllegalArgumentException {
        r: if(!botCommandIDs.containsKey(command.ID)){
            boolean looped = false;
            while(botCommandIDs.inverse().containsKey(lastCommandID++)){
                if(lastCommandID >= maxIDS && looped){
                    Logger.warning("Max command ids reached, unable to add any more.");
                    break r;
                }else if(lastCommandID >= maxIDS){
                    looped = true;
                    lastCommandID = -1;
                }
            }

            botCommandIDs.put(command.ID, lastCommandID);
            botCommands.put(lastCommandID, command);
        }else{
            Logger.warning("Command %s is already registered.", command.ID);
        }
    }

    @Override
    public void registerFilter(ChatFilter filter) {
        r: if(!botFilterIDS.containsKey(filter.ID)){
            boolean looped = false;
            while(botFilterIDS.inverse().containsKey(lastFilterID++)){
                if(lastFilterID >= maxIDS && looped){
                    Logger.warning("Max filter ids reached, unable to add any more.");
                    break r;
                }else if(lastFilterID >= maxIDS){
                    looped = true;
                    lastFilterID = -1;
                }
            }

            botFilterIDS.put(filter.ID, lastFilterID);
            botFilters.put(lastFilterID, filter);
        }else{
            Logger.warning("Filter %s is already registered.", filter.ID);
        }
    }

    @Override
    public Optional<Integer> getFilterID(String id) {
        return Optional.fromNullable(botFilterIDS.get(id));
    }

    @Override
    public Optional<Integer> getCommandID(String id) {
        return Optional.fromNullable(botCommandIDs.get(id));
    }

    @Override
    public Optional<String> getFilterSID(int id) {
        return Optional.fromNullable(botFilterIDS.inverse().get(id));
    }

    @Override
    public Optional<String> getCommandSID(int id) {
        return Optional.fromNullable(botCommandIDs.inverse().get(id));
    }

    @Override
    public boolean loadModule(String id, String server, String channel) {
        Optional<Module> m = moduleManager.getModule(id);
        if(m.isPresent()){
            channelLoadedModules.get(server, channel).add(id);
        }
        return m.isPresent();
    }

    @Override
    public Optional<String> getModuleAlias(String id) {
        return Optional.fromNullable(moduleManager.idsToNames.get(id));
    }

    @Override
    public Optional<String> getModuleSID(String alias) {
        return Optional.fromNullable(moduleManager.idsToNames.inverse().get(alias));
    }

    @Override
    public void unregisterCommand(String id) {
        botCommands.remove(botCommandIDs.get(id));
        botCommandIDs.remove(id);
    }

    @Override
    public void unregisterFilter(String id) {
        botFilters.remove(botFilterIDS.get(id));
        botFilterIDS.remove(id);
    }

    @Override
    public void unregisterModule(String id, String server, String channel) {
        //TODO unload modules no longer in use
        channelLoadedModules.get(server, channel).remove(id);
    }

    @Override
    public void registerChannelModule(String id, String server, String channel) {
        channelLoadedModules.get(server, channel).add(id);
    }

    @Override
    public void unregisterCommand(int id) {
        botCommands.remove(id);
        botCommandIDs.inverse().remove(id);
    }

    @Override
    public void unregisterFilter(int id) {
        botFilters.remove(id);
        botFilterIDS.inverse().remove(id);
    }

    @Override
    public Optional<Command> getModuleCommand(String module, String id) {
        return Optional.fromNullable(botCommands.get(botCommandIDs.get(module + ":" + id)));
    }

    @Override
    public Optional<Command> getModuleCommand(String module, int id) {
        return Optional.fromNullable(botCommands.get(id));
    }

    @Override
    public Optional<List<Command>> getModuleCommands(String moduleID) {
        List<Command> commands = Lists.newArrayList();
        Optional<Module> m = getModule(moduleID);

        if(m.isPresent()){
            m.get().getCommands().forEach(s -> commands.add(getModuleCommand(m.get().getID(), s).get()));
        }
        return commands.size() > 0 ? Optional.of(commands) : Optional.absent();
    }

    @Override
    public Optional<ChatFilter> getModuleFilter(String module, String id) {
        return Optional.fromNullable(botFilters.get(botFilterIDS.get(module + ":" + id)));
    }

    @Override
    public Optional<ChatFilter> getModuleFilter(String module, int id) {
        return Optional.fromNullable(botFilters.get(id));
    }

    @Override
    public Optional<List<ChatFilter>> getModuleFilters(String moduleID) {
        List<ChatFilter> filters = Lists.newArrayList();
        Optional<Module> m = getModule(moduleID);

        if(m.isPresent()){
            m.get().getFilters().forEach(s -> filters.add(getModuleFilter(m.get().getID(), s).get()));
        }
        return filters.size() > 0 ? Optional.of(filters) : Optional.absent();
    }

    @Override
    public boolean doesModuleCommandExist(String module, String id) {
        return getModuleCommand(module, id).isPresent();
    }

    @Override
    public boolean doesModuleFilterExist(String module, String id) {
        return getModuleFilter(module, id).isPresent();
    }

    @Override
    public void registerModuleCommand(String module, Command command) {
        r: if(!botCommandIDs.containsKey(command.ID)){
            boolean looped = false;
            while(botCommandIDs.inverse().containsKey(lastCommandID++)){
                if(lastCommandID >= maxIDS && looped){
                    Logger.warning("Max command ids reached, unable to add any more.");
                    break r;
                }else if(lastCommandID >= maxIDS){
                    looped = true;
                    lastCommandID = -1;
                }
            }

            botCommandIDs.put(module + ":" + command.ID, lastCommandID);
            botCommands.put(lastCommandID, command);
        }else{
            Logger.warning("Command %s:%s is already registered.", module, command.ID);
        }
    }

    @Override
    public void registerModuleFilter(String module, ChatFilter filter) {
        r: if(!botFilterIDS.containsKey(filter.ID)){
            boolean looped = false;
            while(botFilterIDS.inverse().containsKey(lastFilterID++)){
                if(lastFilterID >= maxIDS && looped){
                    Logger.warning("Max filter ids reached, unable to add any more.");
                    break r;
                }else if(lastFilterID >= maxIDS){
                    looped = true;
                    lastFilterID = -1;
                }
            }

            botFilterIDS.put(module + ":" +filter.ID, lastFilterID);
            botFilters.put(lastFilterID, filter);
        }else{
            Logger.warning("Filter %s:%s is already registered.", module, filter.ID);
        }
    }

    @Override
    public void unregisterModuleCommand(String module, String name) {
        unregisterCommand(module + ":" + name);
    }

    @Override
    public void unregisterModuleFilter(String module, String name) {
        unregisterFilter(module + ":" + name);
    }

    @Override
    public void refreshModule(String moduleID) {
        moduleManager.refreshModule(moduleID);
    }

    @Override
    public boolean doesChannelContainModule(String id, String server, String channel) {
        return channelLoadedModules.get(server, channel).contains(id);
    }

    @Override
    public Optional<List<String>> getChannelModules(String server, String channel) {
        return Optional.fromNullable(channelLoadedModules.get(server, channel));
    }

    @Override
    public Optional<Map<String, List<String>>> getChannelsWithModule(String id) {
        Map<String, List<String>> channelsRegistered = Maps.newHashMap();
        //TODO finish this method

        return channelsRegistered.size() > 0 ? Optional.of(channelsRegistered) : Optional.absent();
    }

    @Override
    public List<Command> getBaseCommands() {
        return botCommandIDs.keySet().stream().filter(s -> !s.contains(":")).map(s -> botCommands.get(botCommandIDs.get(s))).collect(Collectors.toList());
    }

    @Override
    public List<ChatFilter> getBaseFilters() {
        return botFilterIDS.keySet().stream().filter(s -> !s.contains(":")).map(s -> botFilters.get(botFilterIDS.get(s))).collect(Collectors.toList());
    }

}