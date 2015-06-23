package com.technostar98.tcbot.command.Commands;

import api.command.Command;
import api.command.CommandType;
import api.command.TextCommand;
import api.filter.ChatFilter;
import api.lib.WrappedEvent;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.command.CommandManager;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.StringJoiner;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class HelpCommand extends Command{

    public HelpCommand(String server){
        super("help", CommandType.USER_MESSAGE, server);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        String[] words = event.getEvent().getMessage().split(" ");

        if(words.length >= 2) {
            CommandManager cm = BotManager.getBotOutputPipe(this.getServer()).getCommandManager(event.getEvent().getChannel().getName());
            if(words[1].equals("allCommands") || words[1].equals("allFilters")) {
                if(words[1].equals("allCommands")){
                    StringJoiner j = new StringJoiner(", ", "{", "}");
                    for(Command c : cm.getCommands()) j.add(c.getName());
                    for(TextCommand c : cm.getTextCommands()) j.add(c.getName());

                    return "Commands for this channel are: " + j.toString();
                }else if(words[1].equals("allFilters")){
                    StringJoiner j1 = new StringJoiner(", ", "{", "}");
                    StringJoiner j2 = new StringJoiner(", ", "{", "}");
                    if(cm.getFilters() != null)
                        for(ChatFilter f : cm.getFilters()) j1.add(f.getName());
                    if(api.command.CommandManager.commandManager.get().getFilters() != null)
                        for(ChatFilter f : api.command.CommandManager.commandManager.get().getFilters()) j2.add(f.getName());
                    if(cm.getModules() != null)
                        for(String module : cm.getModules()) api.command.CommandManager.commandManager.get().getModuleFilters(module).forEach(f -> j2.add(f.getName()));

                    return "Filters enabled in this channel are: " + j1.toString() +
                            "; Filters available are: " + j2.toString();
                }
            }else if(words[1].equals("feature")){
                return "The 'feature' part of !help is WIP. Thank you for you patience.";
            }else if(words[1].equals("command")){
                if(words.length >= 3)
                    return cm.getCommand(words[2]) != null ? cm.getCommand(words[2]).getHelpMessage() : "Command does not exist in this channel.";
                else
                    return "Specify a command.";
            }
        }else{
            return "Improper use of !help, run '!help command help' for further info";
        }

        return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event, Object... args) {
        return true;
    }

    @Override
    public String getHelpMessage() {
        return "!help {allCommands|allFilters|command|feature} <targetCommand>";
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return true;
    }
}
