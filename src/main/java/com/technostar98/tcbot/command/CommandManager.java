package com.technostar98.tcbot.command;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.command.TextCommand;
import com.technostar98.tcbot.api.filter.ChatFilter;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import com.technostar98.tcbot.modules.Module;
import com.technostar98.tcbot.modules.ModuleManager;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;
import java.util.HashMap;
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
    //TODO add modules support
    private ArrayList<Command> commands = new ArrayList<>();
    private ArrayList<ChatFilter> filters = new ArrayList<>();
    private ArrayList<TextCommand> textCommands = new ArrayList<>();
    private HashMap<String, Object> channelValues = new HashMap<>();
    private ArrayList<String> modulesLoaded = new ArrayList<>();
    private final String channel, server;

    public CommandManager(String server, String channel){
        this.channel = channel;
        this.server = server;
        //TODO load values + configs from database
    }

    public List<Command> getCommands(String name){
        return commands.stream().filter(c -> c.getName().equals(name)).collect(Collectors.toList());
    }

    public List<Command> getCommands(){
        return this.commands.isEmpty() ? null : this.commands;
    }

    public List<ChatFilter> getFilters(String name){
        return filters.stream().filter(f -> f.getName().equals(name)).collect(Collectors.toList());
    }

    public List<ChatFilter> getFilters(){
        return this.filters.isEmpty() ? null : this.filters;
    }

    public Command getCommand(String name){
        List<Command> commands = getCommands(name);
        if(commands != null && !commands.isEmpty())
            return commands.get(0);
        else
            return null;
    }

    public ChatFilter getFilter(String name){
        List<ChatFilter> filters = getFilters(name);
        if(filters != null && !filters.isEmpty()) return filters.get(0);
        else return null;
    }

    public List<String> getModules(){
        return this.modulesLoaded;
    }

    public List<String> getModules(String name){
        return modulesLoaded.stream().filter(m -> m.equals(name)).collect(Collectors.toList());
    }

    public String getModule(String name){
        List<String> modules = getModules();
        if(modules != null && !modules.isEmpty())
            return modules.get(0);
        else
            return null;
    }

    public List<TextCommand> getTextCommands(){
        return this.textCommands;
    }

    public List<TextCommand> getTextCommands(String name){
        return this.textCommands.parallelStream().filter(t -> t.getName().equals(name)).collect(Collectors.toList());
    }

    public TextCommand getTextCommand(String name){
        List<TextCommand> t = getTextCommands(name);
        if(t != null && !t.isEmpty())
            return t.get(0);
        else
            return null;
    }

    public HashMap<String, Object> getChannelValues(){
        return this.channelValues;
    }

    public String enactCommand(CommandType type, String name, WrappedEvent<MessageEvent<PircBotX>> event){
//        System.out.println("COMMAND: " + name + "\tUSER: " + event.getEvent().getUser().getPreferredNick());

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

    public void addModule(String name){
        Module m = ModuleManager.getModule(name);

        if(m != null){
            modulesLoaded.add(name);
            HashMap<String, Command> commandsM = m.getCommands();
            HashMap<String, ChatFilter> filtersM = m.getFilters();

            for(String s : commandsM.keySet()){
                addCommand(commandsM.get(s));
                getCommand(s).setServer(server);
            }
            for(String s : filtersM.keySet()){
                addFilter(filtersM.get(s));
                getFilter(s).setServer(server);
            }
        }
    }

    public void addTextCommand(TextCommand textCommand){
        TextCommand tc = getTextCommand(textCommand.name);

        if(tc == null){
            textCommands.add(textCommand);
        }else{
            textCommands.remove(textCommands.indexOf(tc));
            textCommands.add(textCommand);
        }
    }

    public String getChannel() {
        return channel;
    }

    public String getServer() {
        return server;
    }

    public void removeCommand(String name){
        if(getCommand(name) != null)
            commands.remove(commands.indexOf(getCommand(name)));
    }

    public void removeFilter(String name){
        if(getFilter(name) != null)
            filters.remove(filters.indexOf(getFilter(name)));
    }

    public void removeModule(String name){
        Module m = ModuleManager.getModule(name);

        for(String key : m.getCommands().keySet()){
            commands.remove(m.getCommand(key));
        }

        for(String key : m.getFilters().keySet()){
            filters.remove(m.getFilter(key));
        }

        modulesLoaded.remove(name);
    }

    public void removeTextCommand(String name){
        if(getTextCommand(name) != null)
            textCommands.remove(textCommands.indexOf(getTextCommand(name)));
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
        return commands.stream().anyMatch(c -> c.getName().equals(name));
    }

    public boolean hasFilter(String name){
        return filters.stream().anyMatch(c -> c.getName().equals(name));
    }

    public boolean isModuleLoaded(String name){
        return modulesLoaded.contains(name);
    }
}
