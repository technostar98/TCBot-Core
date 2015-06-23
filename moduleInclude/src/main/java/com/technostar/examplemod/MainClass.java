package com.technostar.examplemod;

import api.AssetLoader;
import api.Commands;
import api.Filters;
import api.Module;
import api.command.Command;
import api.filter.ChatFilter;

import java.util.ArrayList;
import java.util.List;

public @Module(id = "exampleModule", name = "Example Module", version = 0) class MainClass {

    @Commands
    public static List<Command> commands = new ArrayList<>();

    @Filters
    public static List<ChatFilter> filters = new ArrayList<>();

    @AssetLoader
    public static void loadCommandsMethod(){
        commands.add(new HelloCommand(null));
        filters.add(new DevinSlapFilter(null));
    }
}
