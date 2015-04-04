package com.technostar.tcbot.bot;

import com.technostar.tcbot.command.CommandManager;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

/**
 * This class is the representation of a bot instance for a specific irc server
 */
public class IRCBot {
    private PircBotX bot;
    private BotState state;

    protected IRCBot(Configuration config, BotState state){
        this.bot = new PircBotX(config);
        this.state = state;
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
}
