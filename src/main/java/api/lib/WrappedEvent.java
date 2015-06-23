package api.lib;

//import Stats;
import org.pircbotx.hooks.Event;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
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
