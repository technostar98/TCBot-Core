package com.technostar98.tcbot.command;

import com.technostar98.tcbot.api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.HashMap;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class TextCommandParser {

    public static String parseCommand(String message, WrappedEvent<MessageEvent<PircBotX>> event, HashMap<String, Object> channelValues){
        return message;
    }
}
