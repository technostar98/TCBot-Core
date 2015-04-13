package com.technostar98.tcbot;

import com.technostar98.tcbot.api.lib.Configuration;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.lib.Logger;
import com.technostar98.tcbot.lib.config.Configs;

/**
 * Main class for launching or relaunching the bot and all of it's other components.
 */
public class Launcher {

    public static void main(String[] args){
        //TODO Load configs/stats
        //TODO logger
        Configuration<String> dir = Configs.getStringConfiguration("workingDir");
        if(dir != null) Logger.info("Working dir = " + dir.getValue());

//        BotManager.createNewBot("irc.esper.net");
        BotManager.createNewBot("irc.technostarhosting.com", "#dev", "#TCBot");
        BotManager.start();

    }
}
