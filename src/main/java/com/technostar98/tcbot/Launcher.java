package com.technostar98.tcbot;

import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.lib.ArgumentConfiguration;
import com.technostar98.tcbot.lib.ArgumentParser;
import com.technostar98.tcbot.lib.config.Configs;
import com.technostar98.tcbot.lib.config.ServerConfiguration;
import com.technostar98.tcbot.lib.config.StartupConfigFileReader;
import com.technostar98.tcbot.lib.exceptions.MissingArgumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class for launching or relaunching the bot and all of it's other components.
 */
public class Launcher {

    public static void main(String[] args){
        //TODO Load configs/stats

        List<ArgumentConfiguration> argsConfigs = new ArrayList<>();
        argsConfigs.add(new ArgumentConfiguration("debugMode", "--", false));
        
        ArgumentParser.loadArguments(argsConfigs);
        try {
            ArgumentParser.parseArguments(args);
        } catch (MissingArgumentException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        StartupConfigFileReader startupConfigFileReader = new StartupConfigFileReader(Configs.getStringConfiguration("configDir").getValue() + "startup.cfg");
        try {
            startupConfigFileReader.readFileContents();
            startupConfigFileReader.closeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configs.setStartupConfigurations(startupConfigFileReader.getMappedContents());

        ServerConfiguration esper = new ServerConfiguration("Espernet", "irc.esper.net", "TCBot", "TEMP", "#TechnoDev");

        BotManager.createNewBot(esper);
//        BotManager.createNewBot("irc.technostarhosting.com", "#dev", "#TCBot");
        BotManager.start();

        if(ArgumentParser.getArgConfig("debugMode").asBoolean()){
            BotManager.startDebugMonitor();
        }
    }
}
