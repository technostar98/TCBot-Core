package com.technostar98.tcbot.bot;

import com.technostar98.tcbot.lib.config.ServerConfiguration;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import java.util.HashMap;

/**
 * <p>This class is the representation of a bot instance for a specific irc server</p>
 *
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class IRCBot {
    public final PircBotX bot;
    private BotState state;
    private String preferredNick;
    public final ServerConfiguration serverConfiguration;
    private HashMap<String, api.lib.Configuration<?>> configs = new HashMap<>();

    protected IRCBot(Configuration config, BotState state, ServerConfiguration configuration){
        this.bot = new PircBotX(config);
        this.state = state;
        this.preferredNick = config.getName();
        this.serverConfiguration = configuration;

        if(!config.getServerHostname().equals("irc.twitch.tv")) {
            configs.put("nickChangeAllowed", new api.lib.Configuration<>(
                    "nickChangeAllowed", true, Boolean.class));
        }else{
            configs.put("nickChangeAllowed", new api.lib.Configuration<>(
                    "nickChangeAllowed", false, Boolean.class));
        }
    }

    public PircBotX getBot() {
        return bot;
    }

    public BotState getState() {
        return state;
    }

    public void setState(BotState state) {
        this.state = state;
    }

    public String getPreferredNick() {
        return preferredNick;
    }

    public void setPreferredNick(String preferredNick) {
        this.preferredNick = preferredNick;
    }

    public api.lib.Configuration<?> getConfiguration(String key){
        return configs.getOrDefault(key, null);
    }

    public ServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }

    public void addConfiguration(String key, api.lib.Configuration configuration){
        configs.put(key, configuration);
    }
}
