package api.filter.event;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class EventContext {
    public final PircBotX bot;
    public final String server;
    public final Channel channel;
    public final User user;
    public final Event event;

    public EventContext(PircBotX bot, String server, Channel channel, User user, Event event){
        this.bot = bot;
        this.server = server;
        this.channel = channel;
        this.user = user;
        this.event = event;
    }
}
