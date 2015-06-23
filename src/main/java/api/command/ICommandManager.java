package api.command;

import api.filter.ChatFilter;
import com.technostar98.tcbot.modules.Module;

import java.util.List;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * All terms and conditions of using said code
 * are determined by the GNU LGPL 3.0.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public interface ICommandManager {

    public Command getCommand(String name);
    public List<Command> getCommands();
    public ChatFilter getFilter(String name);
    public List<ChatFilter> getFilters();
    public Module getModule(String name);
    public List<Module> getModules();
    public boolean doesCommandExist(String name);
    public boolean doesFilterExist(String name);
    public boolean doesModuleExist(String name);
    public void addCommand(String name, Command command);
    public void addCommand(Command command);
    public void addFilter(String name, ChatFilter filter);
    public void addFilter(ChatFilter filter);
    public boolean loadModule(String name, String server, String channel);
    public void removeCommand(String name);
    public void removeFilter(String name);
    public void removeModule(String name, String server, String channel);
    public Command getModuleCommand(String module, String name);
    public List<Command> getModuleCommands(String module);
    public ChatFilter getModuleFilter(String module, String name);
    public List<ChatFilter> getModuleFilters(String module);
    public boolean doesModuleCommandExist(String module, String name);
    public boolean doesModuleFilterExist(String module, String name);
    public void addModuleCommand(String module, Command command);
    public void addModuleFilter(String module, ChatFilter filter);
    public void removeModuleCommand(String module, String name);
    public void removeModuleFilter(String module, String name);
}
