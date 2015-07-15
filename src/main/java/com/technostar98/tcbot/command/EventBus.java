package com.technostar98.tcbot.command;

import api.filter.ChatFilter;
import api.filter.event.Event;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public interface EventBus {
    public void register(ChatFilter event);
    public void unregister(ChatFilter event);
    public void post(Event event);
}
