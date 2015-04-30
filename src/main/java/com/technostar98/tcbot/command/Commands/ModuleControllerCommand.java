package com.technostar98.tcbot.command.Commands;

import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.command.CommandType;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.bot.ListenerPipeline;
import com.technostar98.tcbot.command.CommandManager;
import com.technostar98.tcbot.lib.config.Configs;
import com.technostar98.tcbot.modules.Module;
import com.technostar98.tcbot.modules.ModuleManager;
import org.pircbotx.PircBotX;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.File;
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
public class ModuleControllerCommand extends Command {

    public ModuleControllerCommand(String server){
        super("module", CommandType.USER_MESSAGE, server, UserLevel.OP, UserLevel.OWNER);
    }

    @Override
    public String getMessage(WrappedEvent<MessageEvent<PircBotX>> event) {
        String[] words = event.getEvent().getMessage().split(" ");

        if(!(words.length >= 2 && words[1].equals("list")) && words.length < 3){
            return "Please specify the action (load, unload, or list) and the name of the module";
        }else{
            if(words[1].equals("list")){
                StringJoiner loadedJ = new StringJoiner(", ", "{", "}");
                StringJoiner inDirectory = new StringJoiner(", ", "{", "}");
                File dir = new File(Configs.getStringConfiguration("moduleDir").getValue());
                File[] modules = dir.listFiles();
                ListenerPipeline lP = BotManager.getBotOutputPipe(this.getServer());
                CommandManager cm = lP.getCommandManager(event.getEvent().getChannel().getName());

                for(File f : modules)
                    if(f.getName().endsWith(".jar")) inDirectory.add(f.getName().substring(0, f.getName().indexOf(".jar")));
                if(cm != null)
                    if(cm.getModules() != null)
                        cm.getModules().forEach(m -> loadedJ.add(m));

                return "Modules loaded are " + loadedJ.toString() + "; Available modules are " + inDirectory.toString();
            }else if(words[1].equals("load")){
                String name = words[2];
                ListenerPipeline lP = BotManager.getBotOutputPipe(this.getServer());
                CommandManager cm = lP.getCommandManager(event.getEvent().getChannel().getName());

                if(ModuleManager.loadModule(name)){
                    if(cm.isModuleLoaded(name))
                        return "Module " + name + " is already loaded for this channel.";
                    else {
                        Module m = ModuleManager.getModule(name);
                        cm.addModule(name);
                        return "Module " + name + " has been successfully loaded into this channel with " +
                                m.getCommands().size() + " commands and " + m.getFilters().size()
                                + " filters. Run !help --allCommands or !help --allFilters for further information.";
                    }
                }else{
                    return "There was an error loading the module, please report this and the time in EST to Horfius.";
                }
            }else if(words[1].equals("unload")){
                String name = words[2];
                ListenerPipeline lP = BotManager.getBotOutputPipe(this.getServer());
                CommandManager cm = lP.getCommandManager(event.getEvent().getChannel().getName());

                if(cm.isModuleLoaded(name)){
                    cm.removeModule(name);
                    return "Module " + name + " has been successfully unloaded from this channel.";
                }else{
                    return "Module " + name + " is not loaded into the channel. Nothing unloaded.";
                }
            }else{
                return "Please specify an action (load, unload, or list)";
            }
        }
    }

    @Override
    public boolean runCommand(WrappedEvent<MessageEvent<PircBotX>> event) {
        return false;
    }

    @Override
    public String getHelpMessage() {
        return "!module {list|load|unload} target";
    }
}
