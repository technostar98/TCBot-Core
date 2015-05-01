package com.technostar.examplemod;

import com.technostar98.tcbot.api.AssetLoader;
import com.technostar98.tcbot.api.Commands;
import com.technostar98.tcbot.api.Filters;
import com.technostar98.tcbot.api.Module;
import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.filter.ChatFilter;

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
