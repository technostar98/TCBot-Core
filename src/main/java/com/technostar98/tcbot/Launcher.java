package com.technostar98.tcbot;

import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.lib.ArgumentConfiguration;
import com.technostar98.tcbot.lib.ArgumentParser;
import com.technostar98.tcbot.lib.exceptions.MissingArgumentException;

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

        BotManager.createNewBot("irc.esper.net", "#TechnoDev"/*, "#TheSixPack"*/);
//        BotManager.createNewBot("irc.technostarhosting.com", "#dev", "#TCBot");
        BotManager.start();

        if(ArgumentParser.getArgConfig("debugMode").asBoolean()){
            BotManager.startDebugMonitor();
        }
    }
}
