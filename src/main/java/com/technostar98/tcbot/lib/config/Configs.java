package com.technostar98.tcbot.lib.config;

import com.technostar98.tcbot.Launcher;
import com.technostar98.tcbot.api.lib.Configuration;
import com.technostar98.tcbot.lib.Logger;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>Used to hold and manage any configs for the bot
 * Core configs by name and type are:</p>
 *
 * <p>workingDir - String</p>
 * <p>configDir - String</p>
 * <p>moduleDir - String</p>
 * <p>logDir - String</p>
 * <p>version - String</p>
 *
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
@SuppressWarnings("unchecked, unused")
public class Configs {

    public static ConcurrentHashMap<String, Configuration<?>> configurations = new ConcurrentHashMap<>();
    private static HashMap<String,Configuration<?>> startupConfigurations = new HashMap<>();

    private static final Object ioLock = new Object();

    static{
        loadConfigs();

        char fileSeparator = '/';
        String workingDir = null;

        try {
            workingDir = Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        workingDir = workingDir.substring(0, workingDir.lastIndexOf(fileSeparator));
        workingDir = workingDir.substring(0, workingDir.lastIndexOf(fileSeparator) + 1);
        String configDir = workingDir + "config" + fileSeparator;
        String moduleDir = workingDir + "modules" + fileSeparator;
        String logDir = workingDir + "logs" + fileSeparator;

        File dir = new File(configDir);
        if(!dir.exists()) dir.mkdir();
        dir = new File(moduleDir);
        if(!dir.exists()) dir.mkdir();
        dir = new File(logDir);
        if(!dir.exists()) dir.mkdir();

        addStringConfiguration("workingDir", workingDir);
        addStringConfiguration("configDir", configDir);
        addStringConfiguration("moduleDir", moduleDir);
        addStringConfiguration("logDir", logDir);
        addStringConfiguration("version", "${VERSION}");
    }

    public static void loadConfigs(){
        //TODO load configs from SQL Configs table
        synchronized (ioLock){

        }
    }

    public static void saveConfigs(){
        //TODO save configs to SQL configs table
        synchronized (ioLock){

        }
    }

    public static Configuration<?> getConfiguration(String name){
        Configuration<?> c = configurations.getOrDefault(name, null);

        return c;
    }

    public static Configuration<String> getStringConfiguration(String name){
        Configuration<?> c = getConfiguration(name);

        if(c != null) return (Configuration<String>) c;
        else return null;
    }

    public static Configuration<Character> getCharacterConfiguration(String name){
        Configuration<?> c = getConfiguration(name);

        if(c != null) return (Configuration<Character>) c;
        else return null;
    }

    public static Configuration<Boolean> getBooleanConfiguration(String name){
        Configuration<?> c = getConfiguration(name);

        if(c != null) return (Configuration<Boolean>) c;
        else return null;
    }

    public static Configuration<Integer> getIntConfiguration(String name){
        Configuration<?> c = getConfiguration(name);

        if(c != null) return (Configuration<Integer>) c;
        else return null;
    }

    public static Configuration<Long> getLongConfiguration(String name){
        Configuration<?> c = getConfiguration(name);

        if(c != null) return (Configuration<Long>) c;
        else return null;
    }

    public static Configuration<Short> getShortConfiguration(String name){
        Configuration<?> c = getConfiguration(name);

        if(c != null) return (Configuration<Short>) c;
        else return null;
    }

    public static Configuration<Byte> getByteConfiguration(String name){
        Configuration<?> c = getConfiguration(name);

        if(c != null) return (Configuration<Byte>) c;
        else return null;
    }

    public static Configuration<Double> getDoubleConfiguration(String name){
        Configuration<?> c = getConfiguration(name);

        if(c != null) return (Configuration<Double>) c;
        else return null;
    }

    public static Configuration<Float> getFloatConfiguration(String name){
        Configuration<?> c = getConfiguration(name);

        if(c != null) return (Configuration<Float>) c;
        else return null;
    }

    public static void updateConfig(String name, Object value){
        Configuration<?> c = getConfiguration(name);

        if(c != null) ((Configuration<Object>)c).setValue(value);
        else Logger.warning("Config not found: $1", name);
    }

    public static List<Configuration<?>> getConfigsList(){
        return configurations.keySet().stream().map(s -> configurations.get(s)).collect(Collectors.toList());
    }

    public static ConcurrentHashMap<String, Configuration<?>> getConfigsMap(){
        return configurations;
    }

    public static void addStringConfiguration(String name, String value){
        if(!configurations.containsKey(name)) {
            Configuration<String> c = new Configuration<>(name, value, String.class);

            configurations.put(name, c);
        }else{
            updateConfig(name, value);
        }
    }

    public static void addCharacterConfiguration(String name, Character value){
        if(!configurations.containsKey(name)) {
            Configuration<Character> c = new Configuration<>(name, value, Character.class);

            configurations.put(name, c);
        }else{
            updateConfig(name, value);
        }
    }

    public static void addBooleanConfiguration(String name, Boolean value){
        if(!configurations.containsKey(name)) {
            Configuration<Boolean> c = new Configuration<>(name, value, Boolean.class);

            configurations.put(name, c);
        }else{
            updateConfig(name, value);
        }
    }

    public static void addIntConfiguration(String name, Integer value){
        if(!configurations.containsKey(name)) {
            Configuration<Integer> c = new Configuration<>(name, value, Integer.class);

            configurations.put(name, c);
        }else{
            updateConfig(name, value);
        }
    }

    public static void addLongConfiguration(String name, Long value){
        if(!configurations.containsKey(name)) {
            Configuration<Long> c = new Configuration<>(name, value, Long.class);

            configurations.put(name, c);
        }else{
            updateConfig(name, value);
        }
    }

    public static void addShortConfiguration(String name, Short value){
        if(!configurations.containsKey(name)) {
            Configuration<Short> c = new Configuration<>(name, value, Short.class);

            configurations.put(name, c);
        }else{
            updateConfig(name, value);
        }
    }

    public static void addByteConfiguration(String name, Byte value){
        if(!configurations.containsKey(name)) {
            Configuration<Byte> c = new Configuration<>(name, value, Byte.class);

            configurations.put(name, c);
        }else{
            updateConfig(name, value);
        }
    }

    public static void addDoubleConfiguration(String name, Double value){
        if(!configurations.containsKey(name)) {
            Configuration<Double> c = new Configuration<>(name, value, Double.class);

            configurations.put(name, c);
        }else{
            updateConfig(name, value);
        }
    }

    public static void addFloatConfiguration(String name, Float value){
        if(!configurations.containsKey(name)) {
            Configuration<Float> c = new Configuration<>(name, value, Float.class);

            configurations.put(name, c);
        }else{
            updateConfig(name, value);
        }
    }

    public static void addStringConfigurations(String name, Configuration<String> c){
        if(!configurations.containsKey(name)){
            configurations.put(name, c);
        }else{
            updateConfig(name, c.getValue());
        }
    }

    public static void addCharacterConfigurations(String name, Configuration<Character> c){
        if(!configurations.containsKey(name)){
            configurations.put(name, c);
        }else{
            updateConfig(name, c.getValue());
        }
    }

    public static void addBooleanConfigurations(String name, Configuration<Boolean> c){
        if(!configurations.containsKey(name)){
            configurations.put(name, c);
        }else{
            updateConfig(name, c.getValue());
        }
    }

    public static void addIntConfigurations(String name, Configuration<Integer> c){
        if(!configurations.containsKey(name)){
            configurations.put(name, c);
        }else{
            updateConfig(name, c.getValue());
        }
    }

    public static void addLongConfigurations(String name, Configuration<Long> c){
        if(!configurations.containsKey(name)){
            configurations.put(name, c);
        }else{
            updateConfig(name, c.getValue());
        }
    }

    public static void addShortConfigurations(String name, Configuration<Short> c){
        if(!configurations.containsKey(name)){
            configurations.put(name, c);
        }else{
            updateConfig(name, c.getValue());
        }
    }

    public static void addByteConfigurations(String name, Configuration<Byte> c){
        if(!configurations.containsKey(name)){
            configurations.put(name, c);
        }else{
            updateConfig(name, c.getValue());
        }
    }

    public static void addDoubleConfigurations(String name, Configuration<Double> c){
        if(!configurations.containsKey(name)){
            configurations.put(name, c);
        }else{
            updateConfig(name, c.getValue());
        }
    }

    public static void addFloatConfigurations(String name, Configuration<Float> c){
        if(!configurations.containsKey(name)){
            configurations.put(name, c);
        }else{
            updateConfig(name, c.getValue());
        }
    }

    public static<U> Configuration<U> getCustomConfiguration(String name){
        try{
            return (Configuration<U>)getConfiguration(name);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static<U> void addCustomConfiguration(String name, Configuration<U> configuration){
        configurations.put(name, configuration);
    }

    public static Configuration<?> getStartupConfiguration(String name){
        return startupConfigurations.getOrDefault(name, null);
    }

    public static Configuration<String> getStringStartupConfiguration(String name){
        return (Configuration<String>)getStartupConfiguration(name);
    }

    public static Configuration<String[]> getStringArrayStartupConfiguration(String name){
        return (Configuration<String[]>)getStartupConfiguration(name);
    }

    public static void setStartupConfigurations(HashMap<String, Configuration<?>> configs){
        startupConfigurations = configs;
    }
}