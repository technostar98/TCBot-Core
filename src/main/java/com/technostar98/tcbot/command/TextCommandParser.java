package com.technostar98.tcbot.command;

import api.lib.ValuePair;
import api.lib.WrappedEvent;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.HashMap;
import java.util.function.Function;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class TextCommandParser {
    private static HashMap<String, Function<Double, Double>> mathFunctions = new HashMap<>();
    private static HashMap<String, Function<Double, Double[]>> mathFunctionsMultiArg = new HashMap<>();
    private static HashMap<String, Function<ValuePair<WrappedEvent<MessageEvent<PircBotX>>, String>, String>> stringFunctions = new HashMap<>();
    /*private static Function<ValuePair<String, HashMap<String, Object>>, String> variableFinder = v -> {
        HashMap<String, Object> values = v.getValue2();
        String commandMessage;
    };*/

    static{
        Function<ValuePair<WrappedEvent<MessageEvent<PircBotX>>, String>, String> touser = (v) -> {
            String[] message = v.getValue1().getEvent().getMessage().split(" ");
            if(message.length >= 2)
                return message[1];
            else
                return v.getValue1().getEvent().getUser().getNick();
        };
        Function<ValuePair<WrappedEvent<MessageEvent<PircBotX>>, String>, String> user = v -> v.getValue1().getEvent().getUser().getNick();
        Function<ValuePair<WrappedEvent<MessageEvent<PircBotX>>, String>, String> math = v -> {
            String[] words = v.getValue1().getEvent().getMessage().split(" ");
            //TODO math text

            return "0";
        };
        Function<ValuePair<WrappedEvent<MessageEvent<PircBotX>>, String>, String> wordNumber = v -> {
            String arg = v.getValue2();

            try {
                int index = Integer.valueOf(arg);

                String[] words = v.getValue1().getEvent().getMessage().split(" ");
                if(index < words.length + 1){
                    return words[index + 1];
                }else{
                    return "(out of range)";
                }
            }catch (Exception e){
                return "(invalid index)";
            }
        };
    }

    public static String parseCommand(String message, WrappedEvent<MessageEvent<PircBotX>> event, HashMap<String, Object> channelValues){
        if(message.matches(".*(\\$\\{\\w+\\}).*")){

        }

        return message;
    }

    public static void addMathFunction(String name, Function<Double, Double> function){
        mathFunctions.put(name, function);
    }

    public static void addStringFunction(String name, Function<ValuePair<WrappedEvent<MessageEvent<PircBotX>>, String>, String> function){
        stringFunctions.put(name, function);
    }

    public static void addMathFunctionMultiArg(String name, Function<Double, Double[]> function){
        mathFunctionsMultiArg.put(name, function);
    }
}
