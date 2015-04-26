package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.bot.IRCBot;
import com.technostar98.tcbot.bot.ListenerPipeline;
import com.technostar98.tcbot.command.CommandManager;
import com.technostar98.tcbot.modules.CommandPool;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.stream.Collectors;

public class FilterToggleCommand extends Command{

    public FilterToggleCommand(String server){
        super("filter", CommandType.USER_MESSAGE, server, UserLevel.OP, UserLevel.OWNER, UserLevel.VOICE);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        String[] words = event.getEvent().getMessage().split(" ");
        if(words.length == 1)
            return "Please specify the action ('enable' or 'disable') and target filter";
        else if(words.length == 2)
            return "Please specify the target filter";
        else if(words[2].startsWith("--module=") && words.length >= 4 &&
                doesFilterExist(words[3], words[2].substring(words[2].indexOf("=") + 1))){
            boolean success = runCommand(event);
            if(success)
                return "Filter " + words[3] + " has been successfully " + (words[1] + "d");
            else
                return "Filter " + words[3] + " could not be toggled.";
        }else if(doesFilterExist(words[2], null)){
            boolean success = runCommand(event);
            if(success)
                return "Filter " + words[2] + " has been successfully " + (words[1] + "d");
            else
                return "Filter " + words[2] + " could not be toggled.";
        }

        return "Error: Filter does not exist.";
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        String[] words = event.getEvent().getMessage().split(" ");
        String action = words[1];

        if(words.length >= 4 && words[2].startsWith("--module=")){
            String module = words[2].substring(words[2].indexOf("=") + 1);
            String name = words[3];

            if(CommandPool.getModule(module) != null) {
                CommandManager cm = BotManager.getBotOutputPipe(this.getServer()).getCommandManager(event.getEvent().getChannel().getName());

                if (action.equals("enable")) {
                    if (cm.getFilter(name) != null) {
                        return false;
                    }else {
                        cm.addFilter(CommandPool.getModule(module).getFilter(name));
                        return true;
                    }
                } else if (action.equals("disable")) {
                    if (cm.getFilter(name) != null) {
                        cm.removeFilter(name);
                        return true;
                    }else{
                        return false;
                    }
                }
            }else
                return false;
        }else{
            String name = words[2];

            IRCBot bot = BotManager.getBot(getServer());
            CommandManager cm = ((ListenerPipeline)bot.getBot().getConfiguration().getListenerManager().
                    getListeners().asList().get(1)).getCommandManager(event.getEvent().getChannel().getName());

            if (action.equals("enable")) {
                if (cm.getFilter(name) != null) {
                    return false;
                }else {
                    cm.addFilter(CommandPool.getBotFilterList().stream().filter(f -> f.getName().equals(name))
                            .collect(Collectors.toList()).get(0));
                    return true;
                }
            } else if (action.equals("disable")) {
                if (cm.getFilter(name) != null) {
                    cm.removeFilter(name);
                    return true;
                }else{
                    return false;
                }
            }
        }

        return false;
    }

    @Override
    public String getHelpMessage() {
        return "!filter {enable|disable} <--module=name> filterName";
    }

    private boolean doesFilterExist(String name, String module){
        boolean exists;

        if(module != null){
            if(CommandPool.getModule(module) != null)
                exists = CommandPool.getModule(module).getFilter(name) != null;
            else
                exists = false;
        }else{
            exists = CommandPool.getBotFilterList().stream().anyMatch(f -> f.getName().equals(name));
        }

        return exists;
    }
}
