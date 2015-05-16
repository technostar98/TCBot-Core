package com.technostar98.tcbot.io;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.technostar98.tcbot.lib.IJsonFileIO;
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
public class ChannelValuesFile implements IJsonFileIO<Object> {
    File file = null;
    Map<String, Object> contents = new HashMap<>();

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
    public Map<String, Object> getMappedContents() {
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
    public void setContents(Map<String, Object> contents) {
        this.contents = contents;
    }

    @Override
    public void addField(String key, Object value) {
        contents.put(key, value);
    }

    @Override
    public void saveFileContents() {
        Gson gson = buildGson();
        JsonArray array = new JsonArray();

        for (String s : contents.keySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("name", gson.toJson(s));
            object.addProperty("type", gson.toJson(contents.get(s).getClass().getName()));
            object.addProperty("value", gson.toJson(contents.get(s)));

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

        try{
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);

            HashMap<String, Object> tempMap = new HashMap<>();
            JsonObject[] objects = gson.fromJson(reader, JsonObject[].class);
            if(objects != null){
                for(JsonObject o : objects){
                    String name = gson.fromJson(o.get("name"), String.class);
                    Class type = Class.forName(gson.fromJson("type", String.class));
                    Object value = gson.fromJson(o.get("value"), type);
                    tempMap.put(name, value);
                }

                setContents(tempMap);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isInitialized() {
        return file != null && file.exists();
    }
}
