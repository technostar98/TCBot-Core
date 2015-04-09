package com.technostar98.tcbot.api.filter;

import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.QuitEvent;

public abstract class ChatFilter {
    private final String name;
    private String server;

    public ChatFilter(String name, String server){
        this.name = name;
        this.server = server;
    }

    public abstract boolean onUserMessage(WrappedEvent<MessageEvent<PircBotX>> e);
    public abstract boolean onUserJoin(WrappedEvent<JoinEvent<PircBotX>> e);
    public abstract boolean onUserAction(WrappedEvent<ActionEvent<PircBotX>> e);

    public void setServer(String server) {
        this.server = server;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }
}
