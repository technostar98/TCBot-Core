package com.technostar98.tcbot.command.Filters;

import com.technostar98.tcbot.api.filter.ChatFilter;
import com.technostar98.tcbot.api.filter.FilterResponse;
import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Random;

public class DevinSlapFilter extends ChatFilter {

    public DevinSlapFilter(String server){
        super("devinSlap", server);
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
