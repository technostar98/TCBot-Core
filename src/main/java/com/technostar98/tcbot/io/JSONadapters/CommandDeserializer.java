package com.technostar98.tcbot.io.JSONadapters;

import api.command.TextCommand;
import com.google.gson.*;
import org.pircbotx.UserLevel;

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
public class CommandDeserializer implements JsonDeserializer<TextCommand> {

    @Override
    public TextCommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json instanceof JsonObject){
            JsonObject object = (JsonObject)json;

            String name = object.get("name").getAsString();
            JsonArray ulsArray = object.get("uls").getAsJsonArray();
            UserLevel[] uls = null;
            if(ulsArray.size() > 0) {
                uls = new UserLevel[ulsArray.size()];
                for (int i = 0; i < ulsArray.size(); i++) {
                    uls[i] = UserLevel.valueOf(ulsArray.get(i).getAsString());
                }
            }
            String message = object.get("value").getAsString();

            TextCommand t = null;
            t = uls == null ? new TextCommand(name, null, message) : new TextCommand(name, null, message, uls);

            return t;
        }
        return null;
    }
}
