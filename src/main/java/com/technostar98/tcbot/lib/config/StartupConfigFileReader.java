package com.technostar98.tcbot.lib.config;

import com.technostar98.tcbot.lib.ITextFileIO;
import com.technostar98.tcbot.lib.Logger;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

@Deprecated
public class StartupConfigFileReader implements ITextFileIO<ServerConfiguration>{
    private HashMap<String, ServerConfiguration> configs = new HashMap<>();
    private File configFile = null;
    private BufferedReader input = null;
    private PrintWriter output = null;
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
                    if (!configFile.mkdirs()) {
                        return false;
                    } else {
                        configFile.createNewFile();
                    }
                }

                input = new BufferedReader(new FileReader(configFile));
                output = new PrintWriter(new BufferedWriter(new FileWriter(configFile)));
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
     * The layout of a server configuration file is as goes:
     * S: serverName
     * A: serverAddress
     * N: nick
     * P: password
     * J: channel1
     * J: channel2
     * J: etc...
     * (Space signifying end of one config and start of another)
     * @throws IOException
     */
    @Override
    public void readFileContents() throws IOException{
        synchronized (ioLock){
            String line = null;
            ServerConfiguration sc = null;

            while((line = input.readLine()) != null){

            }
        }
    }

    @Override
    public void saveFileContents() {
        synchronized (ioLock){

        }
    }

    @Override
    public HashMap<String, ServerConfiguration> getMappedContents() {
        return null;
    }

    @Override
    public List<String> getFields() {
        return null;
    }

    @Override
    public void closeFile() {

    }

    @Override
    public void setContents(HashMap<String, ServerConfiguration> contents) {
        this.configs = contents;
    }

    @Override
    public void addField(String key, ServerConfiguration value) {
        configs.put(key, value);
    }
}
