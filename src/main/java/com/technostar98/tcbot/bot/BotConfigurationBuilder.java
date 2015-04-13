package com.technostar98.tcbot.bot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.managers.ListenerManager;
import org.pircbotx.hooks.managers.ThreadedListenerManager;

/**
 * Used to build configurations for different bots
 */
public class BotConfigurationBuilder {
    //TODO get config builder connected to database for reading data about each server

    public static Configuration<PircBotX> buildConfig(String server, String... channels){
        //TODO actual configuration build based upon server, currently hardcoded
        ListenerManager<PircBotX> manager = new ThreadedListenerManager<>();
        manager.addListener(new ListenerPipeline(server, channels)); //TODO Dynamically load channels

        Configuration.Builder<PircBotX> configurationBuilder = new Configuration.Builder<PircBotX>()
                .setAutoNickChange(true)
                .setAutoReconnect(true)
                .setListenerManager(manager)
                .setLogin("TCBot")
                .setMessageDelay(0L)
                .setName("TCBot")
                .setRealName("TCBot")
                .setServer(server, 6667)
                .setVersion("0.0.001");
        for(String s : channels) configurationBuilder.addAutoJoinChannel(s);

        return configurationBuilder.buildConfiguration();
    }
}
