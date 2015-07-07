package com.technostar98.tcbot.bot;

import com.technostar98.tcbot.lib.config.Configs;
import com.technostar98.tcbot.lib.config.ServerConfiguration;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.managers.ListenerManager;
import org.pircbotx.hooks.managers.ThreadedListenerManager;

/**
 * <p>Used to build configurations for different bots</p>
 *
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class BotConfigurationBuilder {
    //TODO get config builder connected to database for reading data about each server

    @Deprecated
    public static Configuration<PircBotX> buildConfig(String server, String... channels){
        //TODO actual configuration build based upon server, currently hardcoded
        ListenerManager<PircBotX> manager = new ThreadedListenerManager<>();
        manager.addListener(new ListenerPipeline(server, channels));

        Configuration.Builder<PircBotX> configurationBuilder = new Configuration.Builder<>()
                .setAutoNickChange(true)
                .setAutoReconnect(true)
                .setListenerManager(manager)
                .setLogin("TCBot")
                .setMessageDelay(0L)
                .setName("TCBot")
                .setRealName("TCBot")
                .setServer(server, 6667)
                .setVersion(Configs.getStringConfiguration("version").getValue());
        for(String s : channels) configurationBuilder.addAutoJoinChannel(s);

        return configurationBuilder.buildConfiguration();
    }

    public static Configuration<PircBotX> buildConfig(ServerConfiguration config){
        ListenerManager<PircBotX> manager = new ThreadedListenerManager<>();
        manager.addListener(new ListenerPipeline(config.getServerAddress(), config.getAutoJoinChannels()));

        Configuration.Builder<PircBotX> configurationBuilder = new Configuration.Builder<>()
                .setAutoNickChange(false)
                .setAutoReconnect(true)
                .setListenerManager(manager)
                .setLogin(config.getNick())
                .setMessageDelay(0L)
                .setName(config.getNick())
                .setRealName(config.getNick())
                .setNickservPassword(config.getPassword())
                .setServer(config.getServerAddress(), 6667)
                .setVersion(Configs.getStringConfiguration("version").getValue());

        config.getAutoJoinChannels().forEach(c -> configurationBuilder.addAutoJoinChannel(c));

        return configurationBuilder.buildConfiguration();
    }
}
