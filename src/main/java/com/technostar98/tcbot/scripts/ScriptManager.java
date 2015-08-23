package com.technostar98.tcbot.scripts;

import com.google.common.cache.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.technostar98.tcbot.lib.ArgumentParser;
import com.technostar98.tcbot.lib.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class ScriptManager {
    private static int maxScriptThreads = 12;
    private static Cache<Integer, Process> processes;
    private static int lastID = -1;
    private static BiMap<Integer, Integer> idMap;

    static{
        ArgumentParser arg = ArgumentParser.INSTANCE();

        if(arg.getArgumentValue("maxScriptThreads") != null){
            try{
                int newCount = Integer.valueOf((String)arg.getArgumentValue("maxScriptThreads"));
                maxScriptThreads = newCount;
            }catch(Exception e){
                e.printStackTrace();
                maxScriptThreads = 12;
            }
        }

        if(maxScriptThreads < 1){
            maxScriptThreads = 12;
            Logger.error("Negative maximum script load.");
        }

        processes = CacheBuilder.newBuilder()
                .concurrencyLevel(maxScriptThreads + (int) (maxScriptThreads * 0.25F))//Give a bit of padding for guaranteed concurrency
                .maximumSize(maxScriptThreads)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
                .removalListener(r -> {
                    if (((Process) r.getValue()).isAlive()) {
                        ((Process) r.getValue()).destroyForcibly();
                    }
                })
                .build();
        idMap = HashBiMap.create(maxScriptThreads);
    }

    public synchronized static Process postScript(ProcessBuilder builder) {
        try {
            boolean looped = false;
            while(processes.getIfPresent(lastID++) != null){
                if(looped && lastID == maxScriptThreads){
                    return null;
                }else if(lastID == maxScriptThreads){
                    lastID = -1;
                    looped = true;
                }
            }
            Process p = builder.start();
            idMap.put(p.hashCode(), lastID);
            processes.put(lastID, p);

            return p;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void freeProcess(Process p){
        processes.invalidate(idMap.get(p.hashCode()));
        idMap.remove(p.hashCode());
        processes.cleanUp();
    }


}
