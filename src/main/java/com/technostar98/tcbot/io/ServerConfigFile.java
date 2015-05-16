package com.technostar98.tcbot.io;


import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.technostar98.tcbot.io.JSONadapters.ServerConfigDeserializer;
import com.technostar98.tcbot.io.JSONadapters.ServerConfigSerializer;
import com.technostar98.tcbot.lib.IJsonFileIO;
import com.technostar98.tcbot.lib.config.Configs;
import com.technostar98.tcbot.lib.config.ServerConfiguration;

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
public class ServerConfigFile implements IJsonFileIO<ServerConfiguration> {
    private File file = null;
    private Map<String, ServerConfiguration> configs = new HashMap<>();

    public ServerConfigFile(){
        openFile(Configs.getStringConfiguration("configDir").getValue() + "servers.json");
    }

    @Override
    public boolean openFile(String url) {
        file = new File(url);

        if(!file.exists()){
            try {
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
    public Map<String, ServerConfiguration> getMappedContents() {
        return configs;
    }

    @Override
    public List<String> getFields() {
        return Lists.newArrayList(configs.keySet());
    }

    @Override
    public void closeFile() {
        saveFileContents();
    }

    @Override
    public void setContents(Map<String, ServerConfiguration> contents) {
        this.configs = contents;
    }

    @Override
    public void addField(String key, ServerConfiguration value) {
        configs.put(key, value);
    }

    @Override
    public void saveFileContents() {
        Gson gson = buildGson();
        JsonArray array = new JsonArray();
        try{
            FileOutputStream writer = new FileOutputStream(file);

            for(String key : configs.keySet()){
                array.add(gson.toJsonTree(configs.get(key)));
            }

            writer.write(gson.toJson(array).getBytes());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readFileContents() throws IOException {
        try{
            Gson gson = buildGson();
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);

            Map<String, ServerConfiguration> tempConfigMap = new HashMap<>();
            ServerConfiguration[] configs = gson.fromJson(reader, ServerConfiguration[].class);
            if(configs != null && configs.length > 0){
                for(ServerConfiguration s : configs){
                    tempConfigMap.put(s.getServerName(), s);
                }
                setContents(tempConfigMap);
            }

            reader.close();
            isr.close();
            inputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isInitialized() {
        return configs != null && !configs.isEmpty();
    }

    @Override
    public Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(ServerConfiguration.class, new ServerConfigSerializer())
                .registerTypeAdapter(ServerConfiguration.class, new ServerConfigDeserializer())
                .disableHtmlEscaping()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }
}
