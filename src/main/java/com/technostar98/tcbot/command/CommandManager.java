package com.technostar98.tcbot.command;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
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
 * Used to hold modules and commands enabled for a bot/channel/whatever
 */
public class CommandManager {
    //TODO add modules support
    private ArrayList<Command> commands = new ArrayList<>();
    private ArrayList<ChatFilter> filters = new ArrayList<>();
    private HashMap<String, Object> channelValues = new HashMap<>();
    private ArrayList<String> modulesLoaded = new ArrayList<>();
    private final String channel, server;

    public CommandManager(String server, String channel){
        this.channel = channel;
        this.server = server;
        //TODO load values + configs from database
    }

    public List<Command> getCommands(String name){
        List<Command> matched = commands.parallelStream().filter(c -> c.getName().equals(name)).collect(Collectors.toList());
        if(!matched.isEmpty())
            return matched;
        else
            return null;
    }

    public List<Command> getCommands(){
        return this.commands.isEmpty() ? null : this.commands;
    }

    public List<ChatFilter> getFilters(String name){
        List<ChatFilter> matched = filters.parallelStream().filter(f -> f.getName().equals(name)).collect(Collectors.toList());
        if (!matched.isEmpty())
            return matched;
        else
            return null;
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
        return this.modulesLoaded.isEmpty() ? null : this.modulesLoaded;
    }

    public List<String> getModules(String name){
        List<String> modules = modulesLoaded.stream().filter(m -> m.equals(name)).collect(Collectors.toList());
        if(!modules.isEmpty())
            return modules;
        else
            return null;
    }

    public String getModule(String name){
        List<String> modules = getModules();
        if(modules != null && !modules.isEmpty())
            return modules.get(0);
        else
            return null;
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
