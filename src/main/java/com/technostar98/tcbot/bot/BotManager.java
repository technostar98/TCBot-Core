package com.technostar98.tcbot.bot;

import com.technostar98.tcbot.lib.Logger;
import com.technostar98.tcbot.lib.config.ServerConfiguration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Listener;

import java.io.IOException;
import java.util.*;

/**
 * <p>Used to manage all the bots and perform actions on all of them</p>
 *
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class BotManager {

    private static HashMap<String, IRCBot> bots = new HashMap<>(); //Bots by server
    private static final Object lock = new Object();
    private static boolean debuggerClosed = false;

    public static void createNewBot(String server, String... channels){
        bots.put(server, new IRCBot(BotConfigurationBuilder.buildConfig(server, channels), BotState.RUNNING));
    }

    public static void createNewBot(ServerConfiguration config){
        bots.put(config.getServerAddress(), new IRCBot(BotConfigurationBuilder.buildConfig(config), BotState.RUNNING));
    }

    public static IRCBot getBot(String server){
        return bots.getOrDefault(server, null);
    }

    public static void start(){
        synchronized (lock) {
            for (final Map.Entry<String, IRCBot> e : bots.entrySet()) {
                Thread run = new Thread(() -> {
                    try {
                        e.getValue().getBot().startBot();
                    } catch (IOException | IrcException f) {
                        f.printStackTrace();
                    }
                });
                run.start();
            }

        }
    }

    public static void stop(){
        synchronized (lock) {
            try {
                for (Map.Entry<String, IRCBot> e : bots.entrySet()) {
                    e.getValue().getBot().stopBotReconnect();
                    e.getValue().getBot().sendIRC().quitServer("Adios");
                    Thread.sleep(50L);
                    e.getValue().getBot().getInputParser().close();

                    bots.remove(e.getKey());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //TODO close anything else necessary
            forceDebuggerShutdown();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopServer(String server){
        synchronized (lock){
            try {
                IRCBot bot = getBot(server);

                if(bot != null){
                    ListenerPipeline l = getBotOutputPipe(server);
                    l.messengerPipeline.setOutputEnabled(false);
                    l.closeListener();

                    bot.getBot().stopBotReconnect();
                    bot.getBot().sendIRC().quitServer("Bye bye...");
                    Thread.sleep(50L);
                    bot.getBot().getInputParser().close();

                    bots.remove(server);
                }

            scheduleDebuggerShutdown();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void changeBotServer(String server){
        synchronized (lock){
            try{
                IRCBot bot = getBot(server);

                if(bot != null){
                    //TODO change server
                }


            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void scheduleDebuggerShutdown(){
        if(bots.size() == 0) debuggerClosed = true;
        else Logger.warning(Thread.currentThread().getName() + " tried to shutdown debugger when it is still active.");
    }

    public static void forceDebuggerShutdown(){
        debuggerClosed = true;
    }

    public static void startDebugMonitor(){
        Thread debuggerThread = new Thread(() -> {
            Calendar c = Calendar.getInstance();
            do {
                Logger.info("Posting debug info for [" + c.getTime() + "]");

                Logger.info("Thread count: " + Thread.activeCount());
                Logger.info("Ram used (bytes): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
                Logger.info("Bot count: " + bots.size());
                Logger.info("Servers connected to: " + bots.keySet().toArray().toString());

                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(!debuggerClosed);
        }, "TCBotDebuggerThread");

        debuggerThread.start();
    }

    public static ListenerPipeline getBotOutputPipe(String server){
        List<Listener<PircBotX>> adapters = getBot(server).getBot().getConfiguration().getListenerManager().getListeners().asList();

        for(Listener l : adapters){
            if(l instanceof ListenerPipeline) return (ListenerPipeline)l;
        }

        return null;
    }
}
