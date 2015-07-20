package com.technostar98.tcbot.lib;

import api.lib.ValuePair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private List<String> garbageArgs = Lists.newLinkedList();

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
    public static final int SPACED = 0b1;
    /**
     * Arg value after a '=', eg. ArgName=Value.
     */
    public static final int EQUALS = 0b10;
    /**
     * Arg value after a ':', eg. ArgName:Value
     */
    public static final int COLON = 0b100;
    /**
     * <p>
     *      Arg value in a map format after a space, eg. {Key1:Value1, Key2:Value2, etc}.
     *      Spaces in the collection will be trimmed off if extraneous.
     * </p>
     * Does <b><i>NOT</i></b> support nested collections.
     */
    public static final int COLLECTION_MAP = 0b1000;
    /**
     * <p>
     *      Arg value in a list format, eg. {Value1, Value2, etc}.
     *      Spaces in the collection will be trimmed off if extraneous.
     * </p>
     * Does <b><i>NOT</i></b> support nested collections.
     */
    public static final int COLLECTION_LIST = 0b10000;

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

    public void processArguments(){
        String[] args0 = argLine.split(" ");
        List<String> args1 = Lists.newLinkedList();
        String conjoinedArg = null;

        for(String arg : args0){
            if(arg.startsWith("{") && conjoinedArg == null){ //If it's the start of a collection and a collection has started
                conjoinedArg = arg;
            }else if(conjoinedArg != null){//Adds trailing collection bits
                conjoinedArg += arg;
                if(arg.endsWith("}")){
                    args1.add(conjoinedArg);
                    conjoinedArg = null;
                }
            }else{
                args1.add(arg);
            }
        }
        args0 = new String[args1.size()];
        for(int i = 0; i < args1.size(); i++) args0[i] = args1.get(i);

        int i = 0;
        while(i < args0.length) {
            ValuePair<Object, ValuePair<String, Integer>> processed = processArg(i, args0);
            if(processed.getValue1() == null){
                garbageArgs.add(processed.getValue2().getValue1());
            }else{
                parsedValues.put(processed.getValue2().getValue1(), processed.getValue1());
            }
            i = processed.getValue2().getValue2();
        }
    }

    private ValuePair<Object, ValuePair<String, Integer>> processArg(int index, String[] argList){
        String arg = argList[index];
        Object value = null;
        if(arg.contains(":")){//Technically COLON and EQUALS checks
            String[] temp = arg.split(":");
            arg = temp[0];
            value = temp[1];
        }else if(arg.contains("=")){
            String[] temp = arg.split("=");
            arg = temp[0];
            value = temp[1];
        }

        if(!necessaryArguments.contains(arg) && !optionalArguments.contains(arg)){//Pulls out garbage args
            return new ValuePair<>(null, new ValuePair<>(arg,++index));
        }

        int readTags;
        if(necessaryArguments.contains(arg)) readTags = necessaryReadTags.get(arg);
        else readTags = optionalReadTags.get(arg);

        try{
            if((readTags & SPACED) == SPACED){
                value = argList[++index];
            }else if((readTags & COLLECTION_MAP) == COLLECTION_MAP){
                String tempVal = argList[++index];
                value = Maps.newHashMap();

                if(!tempVal.startsWith("{") && !tempVal.endsWith("}")) throw new IllegalArgumentException("Wrongly formatted map collection.");
                tempVal = tempVal.substring(1, tempVal.length() - 1);
                String[] tempVals = tempVal.split(",\\ *");

                for(String v : tempVals){
                    v = v.trim();
                    String name, vValue;

                    if(v.contains(":")){
                        String[] vVals = v.split(":");
                        name = vVals[0].trim();
                        vValue = vVals[1].trim();
                    }else if(v.contains("=")){
                        String[] vVals = v.split("=");
                        name = vVals[0].trim();
                        vValue = vVals[1].trim();
                    }else{
                        throw new IllegalArgumentException("Incorrectly formatted map value. Use '=' or ':' when assigning values");
                    }

                    ((Map)value).put(name, vValue);
                }
            }else if((readTags & COLLECTION_LIST) == COLLECTION_LIST){
                String tempVal = argList[++index];
                value = Lists.newLinkedList();

                if(!tempVal.startsWith("{") && !tempVal.endsWith("}")) throw new IllegalArgumentException("Wrongly formatted list collection.");
                tempVal = tempVal.substring(1, tempVal.length() - 1);
                String[] tempVals = tempVal.split(",\\ *");

                for(String v : tempVals){
                    ((List)value).add(v.trim());
                }
            }

            return new ValuePair<>(value, new ValuePair<>(arg, ++index));
        }catch (Exception e){
            System.err.println("Error caught when parsing argument value for arg: " + arg + " at index " + index + ".\nException: " + e.getMessage());
            return new ValuePair<>(null, new ValuePair<>(arg, index++));
        }
    }

    public int missedNecessaryArgs() {
        return getMissedArgs().size();
    }

    public List<String> getMissedArgs(){
        List<String> args = Lists.newLinkedList(necessaryArguments);
        for(Iterator i = parsedValues.keySet().iterator(); i.hasNext();){
            Object temp = i.next();
            if(args.contains(temp)) args.remove(temp);
        }

        return args;
    }

    public List<String> getGarbageArgs(){
        return garbageArgs;
    }

    public Map<String, Object> getParsedValues(){
        return parsedValues;
    }

    public Map<String, Integer> getNecessaryReadTags(){
        return necessaryReadTags;
    }

    public Map<String, Integer> getOptionalReadTags(){
        return optionalReadTags;
    }

    public List<String> getNecessaryArguments(){
        return necessaryArguments;
    }

    public List<String> getOptionalArguments(){
        return optionalArguments;
    }

    public Object getArgumentValue(String arg){
        return parsedValues.get(arg);
    }
}
