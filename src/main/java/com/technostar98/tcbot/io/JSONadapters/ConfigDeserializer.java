package com.technostar98.tcbot.io.JSONadapters;

import com.google.gson.*;
import com.technostar98.tcbot.api.lib.Configuration;

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
public class ConfigDeserializer implements JsonDeserializer<Configuration<?>> {

    @Override
    public Configuration<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(json instanceof JsonObject){
            try {
                JsonObject object = (JsonObject) json;
                String name = object.get("name").getAsString();
                Class<?> type = Class.forName(object.get("type").getAsString());
                Object value = context.deserialize(object.get("value"), type);
                return new Configuration<>(name, value, type);
            }catch(ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
        }else {
            return null;
        }
    }
}
