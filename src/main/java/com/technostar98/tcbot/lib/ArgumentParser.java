package com.technostar98.tcbot.lib;

import com.technostar98.tcbot.lib.exceptions.MissingArgumentException;

import java.util.*;

public class ArgumentParser {
    private static List<ArgumentConfiguration> configurations;
    private static HashMap<String, Integer> configIndexes = new HashMap<>();

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
