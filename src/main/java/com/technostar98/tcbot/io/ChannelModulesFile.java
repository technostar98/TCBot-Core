package com.technostar98.tcbot.io;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
public class ChannelModulesFile implements IJsonFileIO<String> {
    private File file = null;
    private List<String> modules = Lists.newArrayList();

    public ChannelModulesFile(String server, String channel){
        File dir = new File(Configs.getStringConfiguration("configDir").getValue() + server + File.separatorChar + channel + File.separatorChar);
        if(!dir.exists())
            dir.mkdirs();
        openFile(Configs.getStringConfiguration("configDir").getValue() + server + File.separatorChar + channel + File.separatorChar + "modules.json");
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
    public Map<String, String> getMappedContents() {
        HashMap<String, String> map = Maps.newHashMap();
        modules.forEach(m -> map.put(m, m));

        return map;
    }

    @Override
    public List<String> getFields() {
        return modules;
    }

    @Override
    public void closeFile() {

    }

    @Override
    public void setContents(Map<String, String> contents) {
        this.modules = Lists.newArrayList(contents.keySet());
    }

    @Override
    public void addField(String key, String value) {
        modules.add(value);
    }

    @Override
    public void saveFileContents() {
        Gson gson = buildGson();
        JsonArray array = new JsonArray();

        try{
            FileOutputStream outputStream = new FileOutputStream(file);

            for(String s : modules){
                JsonObject object = new JsonObject();
                object.addProperty("module", s);

                array.add(object);
            }

            outputStream.write(gson.toJson(array).getBytes());
            outputStream.close();
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

            List<String> tempModules = Lists.newArrayList();
            JsonObject[] modules = gson.fromJson(reader, JsonObject[].class);
            if(modules != null && modules.length > 0){
                for(JsonObject o : modules){
                    tempModules.add(gson.fromJson(o.get("module"), String.class));
                }

                this.modules = tempModules;
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
        return true;
    }
}
