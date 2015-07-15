package com.technostar98.tcbot.command.Filters;

import api.filter.ChatFilter;
import api.filter.event.ChannelMessageEvent;
import api.filter.event.SubscribedEvent;
import com.technostar98.tcbot.lib.Logger;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class TestFilter extends ChatFilter {

    public TestFilter(String server, String channel){
        super("Test", server, channel, "test");
    }

    @SubscribedEvent
    public void test(ChannelMessageEvent event){
        Logger.info("Message %s from %s on %s", event.getEvent().getMessage(), event.context.channel, event.context.server);
    }
}
