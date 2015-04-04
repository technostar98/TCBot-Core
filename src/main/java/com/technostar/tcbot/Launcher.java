package com.technostar.tcbot;

import com.technostar.tcbot.bot.BotManager;

/**
 * Main class for launching or relaunching the bot and all of it's other components.
 */
public class Launcher {

    public static void main(String[] args){
        //TODO Load configs/stats
        //TODO logger
        BotManager.createNewBot("irc.esper.net");
        BotManager.start();
    }
}
