package com.technostar98.tcbot.io.JSONadapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.technostar98.tcbot.lib.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class ValueDeserializer implements JsonDeserializer<Value> {
    @Override
    public Value deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Value v;
        Value.Type t = Value.Type.values()[json.getAsJsonObject().get("type").getAsInt()];
        Object val = null;
        try {
            val = t.parent.getDeclaredMethod("valueOf", String.class).invoke(null, json.getAsJsonObject().get("value").getAsString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        v = new Value(t, val);

        return v;
    }
}
