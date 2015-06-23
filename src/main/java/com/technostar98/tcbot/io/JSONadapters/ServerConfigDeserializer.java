package com.technostar98.tcbot.io.JSONadapters;

import com.google.gson.*;
import com.technostar98.tcbot.lib.config.ServerConfiguration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * All terms and conditions of using said code
 * are determined by the GNU LGPL 3.0.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ServerConfigDeserializer implements JsonDeserializer<ServerConfiguration> {

    @Override
    public ServerConfiguration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json instanceof JsonObject){
            JsonObject object = (JsonObject) json;
            String serverName = object.get("serverName").getAsString();
            String serverAddress = object.get("serverAddress").getAsString();
            String nick = object.get("nick").getAsString();
            String password = object.get("password").getAsString();
            List<String> channels = new ArrayList<>();
            List<String> superusers = new ArrayList<>();
            object.get("channels").getAsJsonArray().iterator().forEachRemaining(s -> channels.add(s.getAsString()));
            object.get("superusers").getAsJsonArray().iterator().forEachRemaining(u -> superusers.add(u.getAsString()));
            return new ServerConfiguration(serverName, serverAddress, nick, password, superusers, channels);
        }

        return null;
    }
}
