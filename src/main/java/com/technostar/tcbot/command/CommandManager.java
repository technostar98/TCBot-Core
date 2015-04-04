package com.technostar.tcbot.command;

import com.technostar.tcbot.command.Commands.BotQuitCommand;
import com.technostar.tcbot.command.Commands.ShutdownCommand;
import com.technostar.tcbot.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to hold modules and commands enabled for a bot/channel/whatever
 */
public class CommandManager {
    //TODO add module support
    private ArrayList<Command> commands = new ArrayList<>();
    private final String channel, server;

    public CommandManager(String server, String channel){
        this.channel = channel;
        this.server = server;
        //TODO De-hard code command manager
        addCommand(new BotQuitCommand(server));
        addCommand(new ShutdownCommand(server));
    }

    public List<Command> getCommands(String name){
        List<Command> matched = commands.parallelStream().filter(c -> c.getName().equals(name)).collect(Collectors.toList());
        if(!matched.isEmpty()) return matched;
        else return null;
    }

    public String enactCommand(CommandType type, String name, WrappedEvent<MessageEvent<PircBotX>> event){
        System.out.println("COMMAND: " + name + "\tUSER: " + event.getEvent().getUser().getNick());

        List<Command> matched = commands.parallelStream().filter(c -> /*c.getCommandType() == type &&*/
                (c.getName().equals(name) || c.getAlias().equals(name))).collect(Collectors.toList());
        if(!matched.isEmpty()) return matched.get(0).getMessage(event);
        else return null;
    }

    public void addCommand(Command command){
        if(!commands.contains(command)) commands.add(command);
    }

    public String getChannel() {
        return channel;
    }

    public String getServer() {
        return server;
    }
}
