package com.technostar98.tcbot.lib;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.technostar98.tcbot.lib.exceptions.MissingArgumentException;

import java.util.*;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ArgumentParser {
    private List<String> optionalArguments = Lists.newLinkedList();
    private Map<String, Integer> optionalReadTags = Maps.newLinkedHashMap();
    private List<String> necessaryArguments = Lists.newLinkedList();
    private Map<String, Integer> necessaryReadTags = Maps.newLinkedHashMap();
    private final String argLine;
    private Map<String, Object> parsedValues = Maps.newHashMap();

    private static ArgumentParser INSTANCE = null;
    public static final void init(String line) throws RuntimeException{
        if(INSTANCE == null) //Only should be made once, not overriden
            INSTANCE = new ArgumentParser(line);
        else{
            throw new RuntimeException("ArgumentParser.INSTANCE already created, cannot override.");
        }
    }

    public static final ArgumentParser INSTANCE(){
        return INSTANCE;
    }

    /**
     * Arg value after a space, eg. ArgName Value
     */
    public static final int SPACED = 0x01;
    /**
     * Arg value after a '=', eg. ArgName=Value.
     */
    public static final int EQUALS = 0x02;
    /**
     * Arg value after a ':', eg. ArgName:Value
     */
    public static final int COLON = 0x03;
    /**
     * Arg value in a map format after a space, eg. {Key1:Value1, Key2:Value2, etc}.
     * Spaces in the collection will be trimmed off if extraneous.
     */
    public static final int COLLECTION_MAP = 0x04;
    /**
     * Arg value in a list format, eg. {Value1, Value2, etc}.
     * Spaces in the collection will be trimmed off if extraneous.
     */
    public static final int COLLECTION_LIST = 0x05;

    private ArgumentParser(String argLine){
        this.argLine = argLine.trim();
    }

    public void registerOptionalArgument(String argName, int readTag){
        optionalArguments.add(argName);
        optionalReadTags.put(argName, readTag);
    }

    public void registerNecessaryArgument(String argName, int readTag){
        necessaryArguments.add(argName);
        necessaryReadTags.put(argName, readTag);
    }

    

    public static void loadArguments(List<ArgumentConfiguration> args){
        configurations = new LinkedList<>(args);
        configurations.forEach(a -> {
            if(configIndexes.containsKey(a.getArgName()))
                Logger.warning("Multiple argument configurations with name: " + a.getArgName());
            configIndexes.put(a.getArgName(), configurations.indexOf(a));
        });
    }

    public static void parseArguments(String[] args) throws MissingArgumentException{
        for(String s : args){
            configurations.forEach(a ->{
                if(s.startsWith(a.getPrefix())){
                    String preOut = s.substring(s.indexOf(a.getPrefix()) + a.getPrefix().length());
                    if(preOut.indexOf('=') > 0){
                        if(preOut.substring(0, preOut.indexOf('=')).equals(a.getArgName())){
                            String value = preOut.substring(preOut.indexOf('=') + 1);
                            a.setValue(value);
                            a.setFulfilled(true);
                        }
                    }
                }
            });
        }

        List<String> missingArgs = new ArrayList<>();
        configurations.forEach(a ->{
            if(a.isNecessary() && !a.isFulfilled()) {
                missingArgs.add(a.getPrefix() + a.getArgName());
            }
        });

        StringJoiner joiner = new StringJoiner(", ");
        if(missingArgs.size() > 0){
            missingArgs.forEach(a -> joiner.add(a));
            throw new MissingArgumentException("Missing arguments: " + joiner.toString());
        }
    }

    public static ArgumentConfiguration getArgConfig(String name){
        if(Optional.of(name).isPresent())
            if(configIndexes.containsKey(name))
                return configurations.get(configIndexes.get(name));
            else
                return null;
        else
            return null;
    }
}
