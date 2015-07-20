package com.technostar98.tcbot.command.commands;

import api.command.Command;
import api.command.CommandType;
import api.lib.WrappedEvent;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.command.ChannelManager;
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
        super("help", CommandType.USER_MESSAGE, server, "help");
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        String[] words = event.getEvent().getMessage().split(" ");

        if(words.length >= 2) {
            ChannelManager cm = BotManager.getBotOutputPipe(this.getServer()).getChannelManager(event.getEvent().getChannel().getName());
            if(words[1].equals("allCommands") || words[1].equals("allFilters")) {
                if(words[1].equals("allCommands")){
                    StringJoiner j = new StringJoiner(", ", "{", "}");
                    cm.getCommandsList().forEach(c -> j.add(c.getName()));
                    cm.getTextCommandsList().forEach(t -> j.add(t.getName()));

                    return "Commands for this channel are: " + j.toString();
                }else if(words[1].equals("allFilters")){
                    StringJoiner j1 = new StringJoiner(", ", "{", "}");//Enabled
                    StringJoiner j2 = new StringJoiner(", ", "{", "}");//Available
                    if(cm.getEnabledFilters() != null)
                        cm.getEnabledFilters().forEach(f -> {
                            System.out.println(f);
                            j1.add(cm.getFilter(f).get().ID + "|" + cm.getFilter(f).get().name);
                        });
                    if(api.command.CommandManager.commandManager.get().getFilters() != null)
                        cm.getFiltersList().stream().filter(f -> !cm.getEnabledFilters().contains(f.ID)).forEach(f -> j2.add(f.ID + "|" + f.name));

                    return "Filters(by ID|NAME) enabled in this channel are: " + j1.toString() +
                            "; Filters available are: " + j2.toString();
                }
            }else if(words[1].equals("feature")){
                return "The 'feature' part of !help is WIP. Thank you for you patience.";
            }else if(words[1].equals("command")){
                if(words.length >= 3)
                    return cm.getCommand(words[2]) != null ? cm.getCommand(words[2]).get().getHelpMessage() : "Command does not exist in this channel.";
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
