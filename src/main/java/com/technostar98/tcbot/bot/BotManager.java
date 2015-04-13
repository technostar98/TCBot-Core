package com.technostar98.tcbot.bot;

import org.pircbotx.exception.IrcException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to manage all the bots and perform actions on all of them
 */
public class BotManager {

    private static HashMap<String, IRCBot> bots = new HashMap<>(); //Bots by server
    private static final Object lock = new Object();

    public static void createNewBot(String server, String... channels){
        bots.put(server, new IRCBot(BotConfigurationBuilder.buildConfig(server, channels), BotState.RUNNING));
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
                    e.getValue().getBot().sendIRC().quitServer("Devin's a prick.");
                    Thread.sleep(50L);
                    e.getValue().getBot().getInputParser().close();

                    bots.remove(e.getKey());
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

    public static void stopServer(String server){
        synchronized (lock){
            try {
                IRCBot bot = getBot(server);

                if(bot != null){
                    bot.getBot().stopBotReconnect();
                    bot.getBot().sendIRC().quitServer("Bye bye...");
                    Thread.sleep(50L);
                    bot.getBot().getInputParser().close();

                    bots.remove(server);
                }

                if(bots.isEmpty()) System.exit(0);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
