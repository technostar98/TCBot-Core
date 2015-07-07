package com.technostar98.tcbot.bot;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.technostar98.tcbot.io.ServerConfigFile;
import com.technostar98.tcbot.lib.Logger;
import com.technostar98.tcbot.lib.config.ServerConfiguration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Listener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static ServiceManager manager;
    private static HashMap<String, IRCBot> bots = new HashMap<>(); //Bots by server
    private static final Object lock = new Object();
    private static boolean debuggerClosed = false;
    private static Thread dataManager = null;

    public static void createNewBot(ServerConfiguration config){
        bots.put(config.getServerAddress(), new IRCBot(BotConfigurationBuilder.buildConfig(config), BotState.RUNNING, config));
    }

    public static IRCBot getBot(String server){
        return bots.getOrDefault(server, null);
    }

    public static void start(){
        synchronized (lock) {
            List<Service> services = Lists.newLinkedList();
            for (final Map.Entry<String, IRCBot> e : bots.entrySet()) {
                Service s = new AbstractExecutionThreadService() {
                    IRCBot bot = e.getValue();
                    String server = e.getKey();

                    @Override
                    protected void run() throws Exception {
                        Logger.info("Bot for %s server starting up.", server);
                        bot.getBot().startBot();
                    }

                    @Override
                    protected void startUp() throws Exception {
                        super.startUp();
                    }

                    @Override
                    protected void shutDown() throws Exception {
                        super.shutDown();
                        bot.getBot().stopBotReconnect();
                        bot.getBot().sendIRC().quitServer("Adios");
                        getBotOutputPipe(server).messengerPipeline.setOutputEnabled(false);
                        getBotOutputPipe(server).closeListener();
                        try {
                            Thread.sleep(50L);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        bot.getBot().getInputParser().close();
                        bots.remove(server);
                    }

                    @Override
                    protected void triggerShutdown() {
                        super.triggerShutdown();
                        Logger.info("Bot for server %s is shutting down. Closing all tied resources.", server);

                        try {
                            shutDown();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                };
                services.add(s);
            }

            manager = new ServiceManager(services);
            manager.addListener(new ServiceManager.Listener() {
                @Override
                public void healthy() {
                    super.healthy();
                    Logger.info("All bot instances started correctly.");
                }

                @Override
                public void stopped() {
                    super.stopped();
                    Logger.info("All bots shutdown.");
                }

                @Override
                public void failure(Service service) {
                    super.failure(service);
                    try {
                        IRCBot bot = (IRCBot)service.getClass().getField("bot").get(service);
                        Logger.info("Bot for server %s has failed to start.", bot.getServerConfiguration().getServerName());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
            manager.startAsync();

            dataManager = new Thread(() -> {
                while(bots.size() > 0){
                    try {
                        Thread.sleep(600000);
                    } catch (InterruptedException e) {
                        //ignore
                    }

                    bots.keySet().forEach(s -> getBotOutputPipe(s).getCommandManagers().forEach(c -> c.saveChannelData()));
                }
            });
            dataManager.start();
        }
    }

    public static void stop(){
        synchronized (lock) {
            try {
                dataManager.interrupt();
                while(dataManager.getState() != Thread.State.TIMED_WAITING){}

                manager.servicesByState().entries().parallelStream().filter(e -> e.getKey() == Service.State.RUNNING).forEach(e -> e.getValue().stopAsync());
            } catch (Exception e) {
                e.printStackTrace();
            }

            //TODO close anything else necessary
            dataManager.interrupt();
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
                dataManager.interrupt();
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
        else Logger.warning(Thread.currentThread().getName() + " tried to shutdown debugger when bots are still active.");
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

    public static void saveServerConfigurations(){
        synchronized (lock){
            if(bots.size() > 0){
                Logger.info("Saving server configurations!");

                Map<String, ServerConfiguration> configs = Maps.newHashMapWithExpectedSize(bots.size());
                bots.keySet().forEach(b -> configs.put(getBot(b).getServerConfiguration().getServerName(), getBot(b).getServerConfiguration()));

                ServerConfigFile file = new ServerConfigFile();
                file.setContents(configs);
                file.saveFileContents();
            }else{
                Logger.error("Could not find bots to save server configurations for!");
            }
        }
    }
}
