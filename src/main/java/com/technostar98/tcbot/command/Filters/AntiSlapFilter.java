package com.technostar98.tcbot.command.Filters;

import com.technostar98.tcbot.api.filter.ChatFilter;
import com.technostar98.tcbot.api.filter.FilterResponse;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.*;

public class AntiSlapFilter extends ChatFilter{

    public AntiSlapFilter(String server){
        super("slapRetaliator", server, 0);
    }

    @Override
    public FilterResponse onUserMessage(WrappedEvent<MessageEvent<PircBotX>> e) {
        return null;
    }

    @Override
    public FilterResponse onUserJoin(WrappedEvent<JoinEvent<PircBotX>> e) {
        return null;
    }

    @Override
    public FilterResponse onUserAction(WrappedEvent<ActionEvent<PircBotX>> e) {
        if(e.getEvent().getMessage().contains("slaps") && e.getEvent().getMessage().contains(e.getEvent().getBot().getNick())){
            e.getEvent().getBot().sendIRC().action(e.getEvent().getChannel().getName(), "slaps " + e.getEvent().getUser().getNick() + " back in retaliation!");
            return FilterResponse.BREAK;
        }else
            return FilterResponse.CONTINUE;
    }

    @Override
    public FilterResponse onServerConnect(WrappedEvent<ConnectEvent<PircBotX>> e) {
        return null;
    }

    @Override
    public FilterResponse onServerDisconnect(WrappedEvent<DisconnectEvent<PircBotX>> e) {
        return null;
    }

    @Override
    public FilterResponse onChannelDisconnect(WrappedEvent<KickEvent<PircBotX>> e) {
        return null;
    }

    @Override
    public FilterResponse onQuit(WrappedEvent<QuitEvent<PircBotX>> e) {
        return null;
    }

    @Override
    public FilterResponse onSocketConnect(WrappedEvent<SocketConnectEvent<PircBotX>> e) {
        return null;
    }

    @Override
    public FilterResponse onNickPinged(WrappedEvent<MessageEvent<PircBotX>> e) {
        return null;
    }
}
