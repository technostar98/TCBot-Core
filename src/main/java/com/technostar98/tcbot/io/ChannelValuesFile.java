package com.technostar98.tcbot.io;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.technostar98.tcbot.io.JSONadapters.ValueDeserializer;
import com.technostar98.tcbot.io.JSONadapters.ValueSerializer;
import com.technostar98.tcbot.lib.IJsonFileIO;
import com.technostar98.tcbot.lib.Value;
import com.technostar98.tcbot.lib.config.Configs;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * All terms and conditions of using said code
 * are determined by the GNU LGPL 3.0.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ChannelValuesFile implements IJsonFileIO<Value> {
    File file = null;
    Map<String, Value> contents = new HashMap<>();

    public ChannelValuesFile(String server, String channel){
        File dir = new File(Configs.getStringConfiguration("configDir").getValue() + server + File.separatorChar + channel + File.separatorChar);
        if(!dir.exists())
            dir.mkdirs();
        openFile(Configs.getStringConfiguration("configDir").getValue() + server + File.separatorChar + channel + File.separatorChar + "values.json");
    }

    @Override
    public boolean openFile(String url) {
        file = new File(url);

        if(!file.exists()){
            try{
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean openFile(URL url) {
        return this.openFile(url.toString());
    }

    @Override
    public Map<String, Value> getMappedContents() {
        return contents;
    }

    @Override
    public List<String> getFields() {
        return Lists.newArrayList(contents.keySet());
    }

    @Override
    public void closeFile() {

    }

    @Override
    public void setContents(Map<String, Value> contents) {
        this.contents = contents;
    }

    @Override
    public void addField(String key, Value value) {
        contents.put(key, value);
    }

    @Override
    public void saveFileContents() {
        Gson gson = buildGson();
        JsonArray array = new JsonArray();

        for (String s : contents.keySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("name", gson.toJson(s));
            object.addProperty("obj", gson.toJson(contents.get(s)));

            array.add(object);
        }

        try{
            FileOutputStream writer = new FileOutputStream(file);

            writer.write(gson.toJson(array).getBytes());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readFileContents() throws IOException {
        Gson gson = buildGson();

        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(isr);

        HashMap<String, Value> tempMap = new HashMap<>();
        JsonObject[] objects = gson.fromJson(reader, JsonObject[].class);
        if(objects != null){
            for(JsonObject o : objects){
                String name = gson.fromJson(o.get("name"), String.class);
                Value v = gson.fromJson(o.get("obj"), Value.class);
                tempMap.put(name, v);
            }

            setContents(tempMap);
        }
    }

    @Override
    public boolean isInitialized() {
        return file != null && file.exists();
    }

    @Override
    public Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Value.class, new ValueSerializer())
                .registerTypeAdapter(Value.class, new ValueDeserializer())
                .disableHtmlEscaping()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }
}
