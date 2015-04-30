package com.technostar98.tcbot.lib.config;

import com.technostar98.tcbot.api.lib.Configuration;
import com.technostar98.tcbot.lib.ITextFileIO;
import com.technostar98.tcbot.lib.Logger;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StartupConfigFileReader implements ITextFileIO<Configuration<?>>{
    private HashMap<String, Configuration<?>> configs = new HashMap<>();
    private File configFile = null;
    private BufferedReader input = null;
    private final Object ioLock = new Object();

    public StartupConfigFileReader(String url){
        openFile(url);
    }

    public StartupConfigFileReader(URL url){
        openFile(url);
    }

    @Override
    public boolean openFile(String url) {
        synchronized (ioLock) {
            try {
                configFile = new File(url);

                if (!configFile.exists()) {
                    configFile.createNewFile();
                }

                input = new BufferedReader(new FileReader(configFile));
            } catch (IOException e) {
                Logger.error("Could create or open file at directory: " + url);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            if (configFile != null) readFileContents();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean openFile(URL url) {
        return this.openFile(url.toString());
    }

    /**
     * Fields used by TCBot-Core will be:
     * <p>dbaddress, dbprofile, dbpassword, superusers</p>
     * @throws java.io.IOException
     */
    @Override
    public void readFileContents() throws IOException{
        synchronized (ioLock){
            String line = null;

            while((line = input.readLine()) != null){
                if(line.matches("\\w++=\\{?.++}?")){
                    String[] words = line.split("=");
                    String name = words[0];
                    String value = words[1];

                    if(value.startsWith("{") && value.endsWith("}")) {
                        value = value.substring(1, value.length() - 1);
                        String[] values = value.split(",");

                        for(int i = 0; i < values.length; i++){
                            values[i] = values[i].trim();
                        }

                        Configuration<String[]> configuration = new Configuration<>(name, values, String[].class);
                        configs.put(name, configuration);
                    }else{
                        Configuration<String> configuration = new Configuration<>(name, value, String.class);
                        configs.put(name, configuration);
                    }
                }
            }
        }
    }

    @Override
    public void saveFileContents() {
        return; //We don't want to ever save over the startup configuration file, only read it.
    }

    @Override
    public HashMap<String, Configuration<?>> getMappedContents() {
        return this.configs;
    }

    @Override
    public List<String> getFields() {
        return configs.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public void closeFile() {
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setContents(HashMap<String, Configuration<?>> contents) {

    }

    @Override
    public void addField(String key, Configuration<?> value) {

    }
}
