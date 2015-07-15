package api.filter;

import api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.*;

/**
 * <p>Utility class for use with {@link com.technostar98.tcbot.command.EventBus}. Add methods
 * which are annotated with {@link api.filter.event.SubscribedEvent} and have 1 argument which is
 * a child of {@link api.filter.event.Event}</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public abstract class ChatFilter{
    /**
     * Potentially non-unique name of filter.
     */
    public final String name;

    /**
     * <p>ID used for identifying different filters from one another. Must be unique within the same package of filters.</p>
     * <p>Can be the same as ids from other module filters or core bot filters because of how IDs are stored.</p>
     */
    public final String ID;

    private String server, channel;

    public ChatFilter(String name, String server, String channel, String ID){
        this.name = name;
        this.server = server;
        this.channel = channel;
        this.ID = ID;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
