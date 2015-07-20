package com.technostar98.tcbot.io;

import api.command.TextCommand;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.technostar98.tcbot.io.JSONadapters.CommandDeserializer;
import com.technostar98.tcbot.io.JSONadapters.CommandSerializer;
import com.technostar98.tcbot.lib.IJsonFileIO;
import com.technostar98.tcbot.lib.config.Configs;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * All terms and conditions of using said code
 * are determined by the GNU LGPL 3.0.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class CommandsFile implements IJsonFileIO<TextCommand>{
    private File file = null;
    private Map<String, TextCommand> contents = Maps.newHashMap();

    public CommandsFile(String server, String channel){
        File dir = new File(Configs.getStringConfiguration("configDir").getValue() + server + File.separatorChar + channel + File.separatorChar);
        if(!dir.exists())
            dir.mkdirs();
        openFile(Configs.getStringConfiguration("configDir").getValue() + server + File.separatorChar + channel + File.separatorChar + "commands.json");
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
    public Map<String, TextCommand> getMappedContents() {
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
    public void setContents(Map<String, TextCommand> contents) {
        this.contents = contents;
    }

    @Override
    public void addField(String key, TextCommand value) {
        contents.put(key, value);
    }

    @Override
    public void saveFileContents() {
        Gson gson = buildGson();
        JsonArray array = new JsonArray();

        try{
            FileOutputStream writer = new FileOutputStream(file);

            for(String s : contents.keySet()){
                array.add(gson.toJsonTree(contents.get(s)));
            }

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

            TextCommand[] commands = gson.fromJson(reader, TextCommand[].class);
            if(commands != null) {
                setContents(Lists.newArrayList(commands).stream().collect(Collectors.<TextCommand, String, TextCommand>toMap(
                        t -> t.getName(),
                        t -> t
                )));
            }

            inputStream.close();
            isr.close();
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isInitialized() {
        return file != null && file.exists();
    }

    @Override
    public Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(TextCommand.class, new CommandSerializer())
                .registerTypeAdapter(TextCommand.class, new CommandDeserializer())
                .disableHtmlEscaping()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }
}
