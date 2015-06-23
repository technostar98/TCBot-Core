package com.technostar98.tcbot.lib;

import api.lib.Configuration;
import api.lib.Timestamp;
import com.technostar98.tcbot.lib.config.Configs;
import com.technostar98.tcbot.lib.config.Stats;

import java.io.*;
import java.time.LocalDateTime;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class CrashLogBuilder {
    private final Exception e;

    public CrashLogBuilder(Exception e){
        this.e = e;
    }

    public void buildLog(){
        LocalDateTime systemTime = LocalDateTime.now();
        Timestamp programTime = new Timestamp(System.currentTimeMillis() - ((Configuration<Long>)Stats.getStat("startTime")).getValue(), null);

        File log = new File(Configs.getStringConfiguration("logDir").getValue() + "crash" + File.separatorChar + (systemTime.toLocalDate() + "_" + systemTime.toLocalTime()) + ".log");
        File logDir = new File(Configs.getStringConfiguration("logDir").getValue() + "crash" + File.separatorChar);
        try {
            if(!logDir.exists())
                logDir.mkdirs();
            log.createNewFile();
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(log)));

            if(e != null){
                writer.println("An error has occurred! Oh no! D:");
                writer.println("\nStacktrace");
                writer.println("==================================================================");
                writer.println(e.getClass().getName() + ": " + e.getMessage());
                for(StackTraceElement s : e.getStackTrace()){
                    writer.println("\tat " + s.toString());
                }
                writer.println("==================================================================");
                writer.println("\nBot had been running for " + programTime);
                writer.println("Today's date is " + systemTime.toLocalDate() + ", " + systemTime.toLocalTime());

                writer.flush();

                writer.close();
            }else{
                writer.println("Error in writing log file: Exception passes is null.");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
