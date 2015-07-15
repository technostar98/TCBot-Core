package com.technostar98.tcbot.command;

import api.filter.event.Event;
import com.google.common.collect.Multimap;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public interface SubscriberFindingStrategy {
    Multimap<Class<? extends Event>, EventSubscriber> findAllSubscribers(Object source);
}
