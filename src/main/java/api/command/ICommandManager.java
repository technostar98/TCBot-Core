package api.command;

import api.filter.ChatFilter;
import com.google.common.base.Optional;
import com.technostar98.tcbot.modules.Module;

import java.util.List;
import java.util.Map;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * All terms and conditions of using said code
 * are determined by the GNU LGPL 3.0.</p>
 *
 * @author Bret 'Horfius' Dusseault
 * @since 1.0
 */
public interface ICommandManager {

    /**
     * Returns a command based upon ID or name, checking the former before the latter
     * @param name ID or name
     * @return {@link com.google.common.base.Optional} of the command, which may be {@link com.google.common.base.Optional#absent()}
     * @since 1.0
     */
    public Optional<Command> getCommand(String name);

    /**
     * Returns all the registered commands mapped to their string ids
     * @return mapped registry of the commands
     * @since 1.0
     */
    public Map<String, Command> getCommandsS();

    /**
     * Returns all the registered commands mapped to their integer ids
     * @return mapped registry of commands
     * @since 1.0
     */
    public Map<Integer, Command> getCommands();

    /**
     * Returns a filter using passed ID or name, preferring the former over the latter
     * @param name ID or name
     * @return {@link com.google.common.base.Optional} of the filter, which may be {@link com.google.common.base.Optional#absent()}
     * @since 1.0
     */
    public Optional<ChatFilter> getFilter(String name);

    /**
     * Returns all registered filters mapped out by their string ids
     * @return mapped registry of filters
     * @since 1.0
     */
    public Map<String, ChatFilter> getFiltersS();

    /**
     * Returns all filters mapped out by their integer ids
     * @return mapped registry of filters
     * @since 1.0
     */
    public Map<Integer, ChatFilter> getFilters();

    /**
     * Returns a module based upon passed ID or name, preferring the former over the latter.
     * @param name the ID or name
     * @return {@link com.google.common.base.Optional} of the module, which may be {@link com.google.common.base.Optional#absent()}
     * @since 1.0
     */
    public Optional<Module> getModule(String name);

    /**
     * Return all registered modules mapped out by their string ids.
     * @return mapped registry of modules
     * @since 1.0
     */
    public Map<String, Module> getModulesS();

    /**
     * Return all registered modules mapped out by their integer ids.
     * @return mapped registry of modules
     * @since 1.0
     */
    public Map<Integer, Module> getModules();

    /**
     * <p>Checks if a command exists by string ID or name, preferring the former over the latter</p>
     * <p>Module command ids are prefaced with the {@code module_id + ':'}</p>
     * @param name ID or name
     * @return true if found, false if not found
     * @since 1.0
     */
    public boolean doesCommandExist(String name);

    /**
     * <p>Checks if a filter exists by string ID or name, preferring the former over the latter</p>
     * <p>Module filter ids are prefaced with the {@code module_id + ':'}</p>
     * @param name
     * @return true if found, false if not found
     * @since 1.0
     */
    public boolean doesFilterExist(String name);

    /**
     * <p>Checks to see if a module is registered.</p>
     * @param name ID or name, preferring the former over the latter
     * @return true if found, false if not found
     * @since 1.0
     */
    public boolean doesModuleExist(String name);

    /**
     * <p>Used for registering commands. Uses the {@link api.command.Command#ID} for registering so it should not be used for modules registering.</p>
     * @throws java.lang.IllegalArgumentException if the command ID has already been registered
     * @param command Unique command which has not been registered already
     * @since 1.0
     */
    public void registerCommand(Command command) throws IllegalArgumentException;

    /**
     * <p>Used for registering filters. Uses the {@link api.filter.ChatFilter#ID} for tracking the filter in the registry.</p>
     * @param filter
     * @since 1.0
     */
    public void registerFilter(ChatFilter filter);

    /**
     * Attempt to get the integer id of filter using its string id.
     * @param id
     * @return The id matching the passed id, or {@link com.google.common.base.Optional#absent()} if not found.
     * @since 1.0
     */
    public Optional<Integer> getFilterID(String id);

    /**
     * Attempt to get the integer id of a command using its string id.
     * @param id
     * @return The id matching the passed id, or {@link com.google.common.base.Optional#absent()} if not found.
     * @since 1.0
     */
    public Optional<Integer> getCommandID(String id);

    /**
     * Attempt to get the integer id of a module using its string id.
     * @param id
     * @return The id matching the passed id, or {@link com.google.common.base.Optional#absent()} if not found.
     * @since 1.0
     */
    public Optional<Integer> getModuleID(String id);

    /**
     * Attempt to get the {@link java.lang.String} key of a module using its integer id.
     * @param id
     * @return The id of the filter registered with the passed integer id if present or null otherwise.
     * @since 1.0
     */
    public Optional<String> getFilterSID(int id);

    /**
     * Attempt to get the {@link java.lang.String} key of a module using its integer id.
     * @param id
     * @return The id of the filter registered with the passed integer id if present or null otherwise.
     * @since 1.0
     */
    public Optional<String> getCommandSID(int id);

    /**
     * Attempt to get the {@link java.lang.String} key of a module using its integer id.
     * @param id
     * @return The id of the filter registered with the passed integer id if present or null otherwise.
     * @since 1.0
     */
    public Optional<String> getModuleSID(int id);

    /**
     * Tries to load a module using its id.
     * @param id The ID of the module, retrievable from name via {@link api.command.ICommandManager#getModuleSID(String)}
     * @param server Server the load call is originating.
     * @param channel Channel the load call is originating.
     * @return
     */
    public boolean loadModule(String id, String server, String channel);

    /**
     * Get the public name of a module
     * @param id Internal id of desired module
     * @return {@link com.google.common.base.Optional} of the alias, may be {@link com.google.common.base.Optional#absent()} if not found.
     */
    public Optional<String> getModuleAlias(String id);

    /**
     * Get the String ID of a module from its alias. Volatile in that it may return the incorrect ID if more than one module uses the same alias.
     * @param alias Public name of a module
     * @return {@link com.google.common.base.Optional} of the ID, may be {@link com.google.common.base.Optional#absent()} if not found and may not be the originally desired id.
     */
    public Optional<String> getModuleSID(String alias);

    /**
     * Remove a command from the registry by its String ID
     * @param id Internal ID of the desired command.
     */
    public void unregisterCommand(String id);

    /**
     *
     * @param id
     */
    public void unregisterFilter(String id);
    public void unregisterModule(String id, String server, String channel);
    public void registerChannelModule(String id, String server, String channel);
    public void unregisterCommand(int id);
    public void unregisterFilter(int id);
    public void unregisterModule(int id, String server, String channel);
    public Optional<Command> getModuleCommand(String module, String id);
    public Optional<Command> getModuleCommand(String module, int id);
    public Optional<List<Command>> getModuleCommands(String moduleID);
    public Optional<ChatFilter> getModuleFilter(String module, String id);
    public Optional<ChatFilter> getModuleFilter(String module, int id);
    public Optional<List<ChatFilter>> getModuleFilters(String moduleID);
    public boolean doesModuleCommandExist(String module, String id);
    public boolean doesModuleFilterExist(String module, String id);
    public void registerModuleCommand(String module, Command command);
    public void registerModuleFilter(String module, ChatFilter filter);
    public void unregisterModuleCommand(String module, String id);
    public void unregisterModuleFilter(String module, String id);
    public void refreshModule(String moduleID);
    public boolean doesChannelContainModule(String id);
    public Optional<List<String>> getChannelModules(String server, String channel);
    public Optional<Map<String, List<String>>> getChannelsWithModule(String id);
    public List<Command> getBaseCommands();
    public List<ChatFilter> getBaseFilters();
}
