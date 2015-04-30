package com.technostar98.tcbot.api.filter;

import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.*;

public abstract class ChatFilter implements Comparable<ChatFilter>{
    private final String name;
    private final int priority;
    private String server;

    public ChatFilter(String name, String server, int priority){
        this.name = name;
        this.server = server;
        this.priority = priority;
    }

    public abstract FilterResponse onUserMessage(WrappedEvent<MessageEvent<PircBotX>> e);
    public abstract FilterResponse onUserJoin(WrappedEvent<JoinEvent<PircBotX>> e);
    public abstract FilterResponse onUserAction(WrappedEvent<ActionEvent<PircBotX>> e);
    public abstract FilterResponse onServerConnect(WrappedEvent<ConnectEvent<PircBotX>> e);
    public abstract FilterResponse onServerDisconnect(WrappedEvent<DisconnectEvent<PircBotX>> e);
    public abstract FilterResponse onChannelDisconnect(WrappedEvent<KickEvent<PircBotX>> e);
    public abstract FilterResponse onQuit(WrappedEvent<QuitEvent<PircBotX>> e);
    public abstract FilterResponse onSocketConnect(WrappedEvent<SocketConnectEvent<PircBotX>> e);
    public abstract FilterResponse onNickPinged(WrappedEvent<MessageEvent<PircBotX>> e);
    public abstract FilterResponse onUserKick(WrappedEvent<KickEvent<PircBotX>> e);
    public abstract FilterResponse onKicked(WrappedEvent<KickEvent<PircBotX>> e);

    public void close(){

    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(ChatFilter o) {
        return this.getPriority() - o.priority;
    }
}
