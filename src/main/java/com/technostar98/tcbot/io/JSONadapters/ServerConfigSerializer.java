package com.technostar98.tcbot.io.JSONadapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.technostar98.tcbot.lib.config.ServerConfiguration;

import java.lang.reflect.Type;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * All terms and conditions of using said code
 * are determined by the GNU LGPL 3.0.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ServerConfigSerializer implements JsonSerializer<ServerConfiguration> {

    @Override
    public JsonElement serialize(ServerConfiguration src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("serverName", context.serialize(src.getServerName()));
        object.add("serverAddress", context.serialize(src.getServerAddress()));
        object.add("nick", context.serialize(src.getNick()));
        object.add("password", context.serialize(src.getPassword()));
        object.add("channels", context.serialize(src.getAutoJoinChannels()));
        return object;
    }
}
