package com.technostar98.tcbot.io.JSONadapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.technostar98.tcbot.api.command.TextCommand;

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
public class CommandSerializer implements JsonSerializer<TextCommand> {

    @Override
    public JsonElement serialize(TextCommand src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("name", context.serialize(src.getName()));
        object.add("uls", context.serialize(src.getRequiredULs()));
        object.add("value", context.serialize(src.getMessage(null)));
        return object;
    }
}
