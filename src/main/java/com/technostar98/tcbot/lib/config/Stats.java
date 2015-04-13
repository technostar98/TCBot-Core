package com.technostar98.tcbot.lib.config;

import com.technostar98.tcbot.api.lib.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to hold any stats.
 */
public class Stats {

    public static ArrayList<Configuration<?>> stats = new ArrayList<>();

    static{

    }

    public static void addStat(Configuration newStat){
        if(!stats.contains(newStat)) stats.add(newStat);
    }

    public static void updateStat(String name, Object value){
        List<Configuration> matched = collectMatches(name);
        if(matched != null && matched.size() > 0){
//            stats.get(stats.indexOf(matched.get(0))).setValue(value); TODO Get updating by value working
        }
    }

    public static void updateStat(String name, Configuration value){
        List<Configuration> matched = collectMatches(name);
        if(matched != null && matched.size() > 0){
            stats.remove(value);
            stats.add(value);
        }
    }

    public static Configuration getStat(String name){
        List<Configuration> matched = collectMatches(name);
        if(matched != null && matched.size() > 0) return matched.get(0);
        else return null;
    }

    public static List<Configuration> collectMatches(String name){
        if(containsStat(name))
            return stats.parallelStream().filter(c -> c.getName().equals(name)).collect(Collectors.toList());
        else
            return null;
    }

    public static boolean containsStat(String name){
        return stats.parallelStream().anyMatch(c -> c.getName().equals(name));
    }
}
