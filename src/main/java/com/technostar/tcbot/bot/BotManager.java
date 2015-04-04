package com.technostar.tcbot.bot;

import org.pircbotx.exception.IrcException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to manage all the bots and perform actions on all of them
 */
public class BotManager {

    private static HashMap<String, IRCBot> bots = new HashMap<>(); //Bots by server
    private static Object lock = new Object();

    public static void createNewBot(String server){
        bots.put(server, new IRCBot(BotConfigurationBuilder.buildConfig(server), BotState.RUNNING));
    }

    public static IRCBot getBot(String server){
        return bots.getOrDefault(server, null);
    }

    public static void start(){
        synchronized (lock) {
            for (final Map.Entry<String, IRCBot> e : bots.entrySet()) {
                Thread run = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            e.getValue().getBot().startBot();
                        } catch (IOException | IrcException e) {
                            e.printStackTrace();
                        }
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
                    e.getValue().getBot().sendIRC().quitServer();
                    e.getValue().getBot().getInputParser().close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //TODO close anything else necessary
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}
