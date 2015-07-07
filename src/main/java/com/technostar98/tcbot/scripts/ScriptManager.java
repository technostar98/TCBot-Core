package com.technostar98.tcbot.scripts;

import com.google.common.collect.Lists;
import com.technostar98.tcbot.lib.ArgumentParser;

import java.io.IOException;
import java.util.List;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class ScriptManager {
    private static int maxScriptThreads = 12;
    private static List<Integer> processHashes = Lists.newArrayList();

    static{
        ArgumentParser arg = ArgumentParser.INSTANCE();

        if(arg.getArgumentValue("maxScriptThreads") != null){
            try{
                int newCount = Integer.valueOf((String)arg.getArgumentValue("maxScriptThreads"));
                maxScriptThreads = newCount;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static Process postScript(ProcessBuilder builder) {
        try {
            while(processHashes.size() >= maxScriptThreads){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Process p = builder.start();
            processHashes.add(p.hashCode());

            return p;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void freeProcess(int hash){
        processHashes.remove((Object)hash);
    }
}
