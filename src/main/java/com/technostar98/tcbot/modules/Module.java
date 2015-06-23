package com.technostar98.tcbot.modules;

import api.command.CommandManager;
import api.command.ICommandManager;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class Module {
    private final List<String> commands = new ArrayList<>();
    private final List<String> filters = new ArrayList<>();
    private final String name, ID;
    private final int version;

    public Module(String name, String id, int version){
        this.name = name;
        this.ID = id;
        this.version = version;

        final ICommandManager manager = CommandManager.commandManager.get();
        manager.getModuleCommands(name).forEach(c -> commands.add(c.getName()));
        manager.getModuleFilters(name).forEach(c -> commands.add(c.getName()));
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public int getVersion() {
        return version;
    }

    public List<String> getCommands(){
        return this.commands;
    }

    public List<String> getFilters(){
        return this.filters;
    }
}
