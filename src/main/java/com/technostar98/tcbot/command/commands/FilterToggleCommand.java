package com.technostar98.tcbot.command.commands;

import api.command.Command;
import api.command.CommandType;
import api.command.ICommandFilterRegistry;
import api.lib.WrappedEvent;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.command.ChannelManager;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class FilterToggleCommand extends Command{

    public FilterToggleCommand(String server){
        super("filter", CommandType.USER_MESSAGE, server, "filter", UserLevel.OP, UserLevel.OWNER, UserLevel.VOICE);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        String[] words = event.getEvent().getMessage().split(" ");
        ICommandFilterRegistry manager = api.command.CommandManager.commandManager.get();

        if(words.length == 1)
            return "Please specify the action ('enable' or 'disable') and target filter";
        else if(words.length == 2)
            return "Please specify the target filter";
        else if(manager.doesFilterExist(words[2]) || manager.doesModuleFilterExist("*", words[2])){
            boolean success = runCommand(event, words[1], words[2], manager);
            if(success)
                return "Filter " + words[2] + " has been successfully " + (words[1] + "d");
            else
                return "Filter " + words[2] + " could not be toggled.";
        }

        return "Error: Filter does not exist.";
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event, Object... args) {
        String action = (String)args[0];
        String target = (String)args[1];
        ICommandFilterRegistry manager = (ICommandFilterRegistry)args[2];
        ChannelManager channelManager = BotManager.getBotOutputPipe(getServer()).getChannelManager(event.getEvent().getChannel().getName());

        if(action.equals("enable")){
            if(!channelManager.hasFilter(name) && channelManager.isFilterAvailable(target)){
                if(manager.getFilter(target) != null) {
                    channelManager.addFilter(manager.getFilter(target).get());
                    return true;
                }
            }
        }else if(action.equals("disable")){
            if(channelManager.hasFilter(target)){
                channelManager.removeFilter(target);
                return true;
            }
        }

        return false;
    }

    @Override
    public String getHelpMessage() {
        return "!filter {enable|disable} <--module=name> filterName";
    }


}
