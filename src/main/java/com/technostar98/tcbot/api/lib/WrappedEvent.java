package com.technostar98.tcbot.api.lib;

//import Stats;
import org.pircbotx.hooks.Event;

/**
 *
 */
public class WrappedEvent<E extends Event<?>> {
    private final E event;
//    private final Timestamp timestamp = new Timestamp(System.currentTimeMillis() - (Long) Stats.getStat("bot.starttime").getValue(), null);

    public WrappedEvent(E e){
        this.event = e;
    }

    public E getEvent() {
        return event;
    }

    /*public Timestamp getTimestamp() {
        return timestamp;
    }*/
}
