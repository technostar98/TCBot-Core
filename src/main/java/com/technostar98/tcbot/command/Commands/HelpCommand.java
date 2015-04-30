package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.filter.ChatFilter;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.bot.IRCBot;
import com.technostar98.tcbot.bot.ListenerPipeline;
import com.technostar98.tcbot.command.CommandManager;
import com.technostar98.tcbot.modules.CommandPool;
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
            if(words[1].equals("--command") || words[1].equals("--allCommands") || words[1].equals("--allFilters")) {
                CommandManager cm = BotManager.getBotOutputPipe(this.getServer()).getCommandManager(event.getEvent().getChannel().getName());

                if(words[1].equals("--command")){
                    if(words.length < 3) {
                        return "Please specify a command to get help on.";
                    }else{
                        if(cm.getCommand(words[2]) != null)
                            return cm.getCommand(words[2]).getHelpMessage();
                        else
                            return "Command " + words[2] + " does not exist for this channel.";
                    }
                }else if(words[1].equals("--allCommands")){
                    StringJoiner j = new StringJoiner(", ", "{", "}");
                    for(Command c : cm.getCommands()) j.add(c.getName());

                    return "Commands for this channel are: " + j.toString();
                }else if(words[1].equals("--allFilters")){
                    StringJoiner j1 = new StringJoiner(", ", "{", "}");
                    StringJoiner j2 = new StringJoiner(", ", "{", "}");
                    for(ChatFilter f : cm.getFilters()) j1.add(f.getName());
                    for(ChatFilter f : CommandPool.getBotFilterList()) j2.add(f.getName());

                    return "Filters enabled in this channel are: " + j1.toString() +
                            "; Filters available are: " + j2.toString();
                }
            }else if(words[1].equals("--feature")){
                return "The 'feature' part of !help is WIP. Thank you for you patience.";
            }
        }else{
            return "Improper use of !help, run '!help --command help' for further info";
        }

        return null;
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        return true;
    }

    @Override
    public String getHelpMessage() {
        return "!help {--allCommands|--allFilters|--command|--feature} <targetCommand>";
    }

    @Override
    public boolean isUserAllowed(MessageEvent<PircBotX> event) {
        return true;
    }
}
