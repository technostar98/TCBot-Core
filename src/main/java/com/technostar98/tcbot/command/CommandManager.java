package com.technostar98.tcbot.command;

import api.command.Command;
import api.command.ICommandManager;
import api.command.TextCommand;
import api.filter.ChatFilter;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.technostar98.tcbot.io.ChannelModulesFile;
import com.technostar98.tcbot.io.ChannelValuesFile;
import com.technostar98.tcbot.io.CommandsFile;
import com.technostar98.tcbot.lib.Logger;
import com.technostar98.tcbot.modules.Module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Used to hold modules and commands enabled for a bot/channel/whatever</p>
 *
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class CommandManager {
    private Map<String, Command> commands = Maps.newHashMap();
    private Map<String, ChatFilter> filters = Maps.newHashMap();
    private Map<String, TextCommand> textCommands = Maps.newHashMap();
    private Map<String, Object> channelValues = Maps.newHashMap();
    private ArrayList<String> modulesLoaded = new ArrayList<>();
    public final EventBus eventBus = new EventBus();
    public final String channel, server;

    public CommandManager(String server, String channel){
        this.channel = channel;
        this.server = server;
        loadChannelData();
        //TODO load values + configs from database
    }

    public List<Command> getCommandsList(){
        return this.commands.keySet().stream().map(k -> commands.get(k)).collect(Collectors.toList());
    }

    public Map<String, Command> getCommands(){
        return commands;
    }

    public Optional<Command> getCommand(String name){
        Map commands = getCommands();
        return Optional.fromNullable((Command) commands.get(name));
    }

    public Map<String, ChatFilter> getFilters(){
        return filters;
    }

    public Optional<ChatFilter> getFilter(String name){
        return Optional.fromNullable(getFilters().get(name));
    }

    public List<String> getModules(){
        return this.modulesLoaded;
    }

    public boolean isModuleLoaded(String name){
        return modulesLoaded.indexOf(name) >= 0;
    }

    public List<TextCommand> getTextCommandsList(){
        return this.textCommands.keySet().stream().map(k -> textCommands.get(k)).collect(Collectors.toList());
    }

    public Map<String, TextCommand> getTextCommands(){
        return this.textCommands;
    }

    public Optional<TextCommand> getTextCommand(String name){
        Map commands = getTextCommands();
        return Optional.fromNullable((TextCommand)commands.get(name));
    }

    public Map<String, Object> getChannelValues(){
        return this.channelValues;
    }

    public void addCommand(Command command){
        command.setServer(server);
        commands.put(command.getName(), command);
    }

    public void addFilter(ChatFilter filter){
        filter.setServer(server);
        eventBus.register(filter);
    }

    public void addModule(String name){
        ICommandManager manager = api.command.CommandManager.commandManager.get();
        manager.loadModule(name, server, channel);
        Optional<Module> m = Optional.fromNullable(manager.getModule(name));

        if(m.isPresent()){
            modulesLoaded.add(name);

            for(String s : m.get().getCommands()){
                if(getCommand(s).isPresent()){
                    Logger.warning("Tried overwriting command %s in channel %s on server %s.", s, channel, server);
                    continue;
                }
                addCommand(manager.getModuleCommand(m.get().getName(), s));
            }
            for(String s : m.get().getFilters()){
                addFilter(manager.getModuleFilter(m.get().getName(), s));
            }
        }
    }

    public void addTextCommand(TextCommand textCommand){
        textCommands.put(textCommand.name, textCommand);
    }

    public void removeCommand(String name){
        commands.remove(name);
    }

    public void removeFilter(String name){
        eventBus.unregister(getFilter(name));
        filters.remove(name);
    }

    public void removeModule(String name){
        ICommandManager manager = api.command.CommandManager.commandManager.get();
        Module m = manager.getModule(name);

        for(String key : m.getCommands()){
            commands.remove(manager.getModuleCommand(name, key));
        }

        for(String key : m.getFilters()){
            filters.remove(manager.getModuleFilter(name, key));
        }

        modulesLoaded.remove(name);
        manager.removeModule(name, server, channel);
    }

    public void removeTextCommand(String name){
        textCommands.remove(name);
    }

    public void setValue(String key, Object value){
        this.channelValues.put(key, value);
    }

    public Object getValue(String key){
        return channelValues.getOrDefault(key, null);
    }

    public void saveValues(){
        //TODO save values map
    }

    public boolean hasCommand(String name){
        return commands.containsKey(name);
    }

    public boolean hasFilter(String name){
        return filters.containsKey(name);
    }

    public boolean isFilterAvailable(String name){
        ICommandManager manager = api.command.CommandManager.commandManager.get();
        return manager.getFilter(name) != null || modulesLoaded.stream().anyMatch(s -> manager.getModuleFilter(s, name) != null);
    }

    public void saveChannelData(){
        CommandsFile commandsFile = new CommandsFile(server, channel);
        ChannelValuesFile channelValuesFile = new ChannelValuesFile(server, channel);
        ChannelModulesFile modulesFile = new ChannelModulesFile(server, channel);

        commandsFile.setContents(textCommands);
        channelValuesFile.setContents(this.channelValues);
        modulesFile.setContents(modulesLoaded.stream().collect(Collectors.<String, String, String>toMap(
                module -> module,
                module -> module
        )));

        commandsFile.saveFileContents();
        channelValuesFile.saveFileContents();
        modulesFile.saveFileContents();
    }

    private void loadChannelData(){
        CommandsFile commandsFile = new CommandsFile(server, channel);
        ChannelValuesFile channelValuesFile = new ChannelValuesFile(server, channel);
        ChannelModulesFile modulesFile = new ChannelModulesFile(server, channel);

        try {
            commandsFile.readFileContents();
            channelValuesFile.readFileContents();
            modulesFile.readFileContents();

            Map<String, TextCommand> commands = commandsFile.getMappedContents();
            Map<String, Object> values = channelValuesFile.getMappedContents();
            List<String> modules = modulesFile.getFields();
            this.textCommands = Maps.newHashMap(commands);
            this.channelValues = Maps.newHashMap(values);

            modules.forEach(m -> {
                if(!isModuleLoaded(m)){
                    addModule(m);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
