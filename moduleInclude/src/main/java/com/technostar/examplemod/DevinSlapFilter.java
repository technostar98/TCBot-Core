package com.technostar.examplemod;

import api.filter.ChatFilter;
import api.filter.FilterResponse;
import api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.*;

import java.util.Random;

public class DevinSlapFilter extends ChatFilter {

    public DevinSlapFilter(String server){
        super("devinSlap", server, 0);
    }

    @Override
    public FilterResponse onServerConnect(WrappedEvent<ConnectEvent<PircBotX>> e) {
        return null;
    }

    @Override
    public FilterResponse onNickPinged(WrappedEvent<MessageEvent<PircBotX>> e) {
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
    public FilterResponse onUserMessage(WrappedEvent<MessageEvent<PircBotX>> e) {
        if(e.getEvent().getUser().getNick().toLowerCase().contains("devin")){
            Random r = new Random();
            float f = r.nextFloat();

            if(f >= 0.9F){
                e.getEvent().getBot().sendIRC().action(e.getEvent().getChannel().getName(), "slaps " + e.getEvent().getUser().getNick()
                    + " around a bit with a large trout.");
            }
        }
        return FilterResponse.CONTINUE;
    }

    @Override
    public FilterResponse onUserJoin(WrappedEvent<JoinEvent<PircBotX>> e) {
        return FilterResponse.CONTINUE;
    }

    @Override
    public FilterResponse onUserAction(WrappedEvent<ActionEvent<PircBotX>> e) {
        return FilterResponse.CONTINUE;
    }
}
