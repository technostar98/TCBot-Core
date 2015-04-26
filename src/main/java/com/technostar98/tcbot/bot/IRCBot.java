package com.technostar98.tcbot.bot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import java.util.HashMap;

/**
 * This class is the representation of a bot instance for a specific irc server
 */
public class IRCBot {
    private PircBotX bot;
    private BotState state;
    private String nick;
    private HashMap<String, com.technostar98.tcbot.api.lib.Configuration<?>> configs = new HashMap<>();

    protected IRCBot(Configuration config, BotState state){
        this.bot = new PircBotX(config);
        this.state = state;

        if(!config.getServerHostname().equals("irc.twitch.tv")) {
            configs.put("nickChangeAllowed", new com.technostar98.tcbot.api.lib.Configuration<>(
                    "nickChangeAllowed", true, Boolean.class));
        }else{
            configs.put("nickChangeAllowed", new com.technostar98.tcbot.api.lib.Configuration<>(
                    "nickChangeAllowed", false, Boolean.class));
        }
    }

    public PircBotX getBot() {
        return bot;
    }

    public void setBot(PircBotX bot) {
        this.bot = bot;
    }

    public BotState getState() {
        return state;
    }

    public void setState(BotState state) {
        this.state = state;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public com.technostar98.tcbot.api.lib.Configuration<?> getConfiguration(String key){
        return configs.getOrDefault(key, null);
    }
}
