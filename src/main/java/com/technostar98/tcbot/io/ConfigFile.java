package com.technostar98.tcbot.io;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.technostar98.tcbot.api.lib.Configuration;
import com.technostar98.tcbot.io.JSONadapters.ConfigDeserializer;
import com.technostar98.tcbot.io.JSONadapters.ConfigSerializer;
import com.technostar98.tcbot.lib.IJsonFileIO;

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
public class ConfigFile implements IJsonFileIO<Configuration<?>>{
    private File file = null;
    private Map<String, Configuration<?>> configMap = new HashMap<>();

    public ConfigFile(String path){
        openFile(path);
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
    public Map<String, Configuration<?>> getMappedContents() {
        return configMap;
    }

    @Override
    public List<String> getFields() {
        return Lists.newArrayList(configMap.keySet());
    }

    @Override
    public void closeFile() {
        saveFileContents();
    }

    @Override
    public void setContents(Map<String, Configuration<?>> contents) {
        this.configMap.clear();
        this.configMap = contents;
    }

    @Override
    public void addField(String key, Configuration<?> value) {
        configMap.put(key, value);
    }

    @Override
    public void saveFileContents() {
        Gson gson = buildGson();
        JsonArray master = new JsonArray();
        try {
            FileOutputStream writer = new FileOutputStream(file);

            for(String key : configMap.keySet()){
                master.add(gson.toJsonTree(configMap.get(key)));
            }

            writer.write(gson.toJson(gson.toJsonTree(master)).getBytes());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readFileContents() throws IOException {
        try {
            Gson gson = buildGson();
            FileInputStream input = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);

            Map<String, Configuration<?>> tempConfigMap = new HashMap<>();
            Configuration[] configs = gson.fromJson(reader, Configuration[].class);
            if(configs != null && configs.length > 0) {
                for (Configuration c : configs) {
                    tempConfigMap.put(c.getName(), c);
                }
                setContents(tempConfigMap);
            }

            input.close();
            isr.close();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Gson buildGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder
                .registerTypeAdapter(Configuration.class, new ConfigSerializer())
                .registerTypeAdapter(Configuration.class, new ConfigDeserializer())
                .disableHtmlEscaping()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public boolean isInitialized() {
        return !configMap.isEmpty();
    }
}
