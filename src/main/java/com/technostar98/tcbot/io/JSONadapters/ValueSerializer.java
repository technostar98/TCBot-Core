package com.technostar98.tcbot.io.JSONadapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.technostar98.tcbot.lib.Value;

import java.lang.reflect.Type;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class ValueSerializer implements JsonSerializer<Value> {
    @Override
    public JsonElement serialize(Value src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", src.type.ordinal());
        obj.add("value", context.serialize(src.getValue()));
        return obj;
    }
}
