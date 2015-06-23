package com.technostar98.tcbot;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import api.command.CommandManager;
import api.command.ICommandManager;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.io.ConfigFile;
import com.technostar98.tcbot.io.ServerConfigFile;
import com.technostar98.tcbot.lib.ArgumentConfiguration;
import com.technostar98.tcbot.lib.ArgumentParser;
import com.technostar98.tcbot.lib.CrashLogBuilder;
import com.technostar98.tcbot.lib.config.Configs;
import com.technostar98.tcbot.lib.config.ServerConfiguration;
import com.technostar98.tcbot.lib.config.Stats;
import com.technostar98.tcbot.lib.exceptions.MissingArgumentException;
import com.technostar98.tcbot.modules.CommandPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>Main class for launching or relaunching the bot and all of it's other components.</p>
 *
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class Launcher {

    public static void main(String[] args){
        //TODO Load configs/stats

        try {
            Stats.getStat("startTime").getValue();//Make sure <cinit> is called in stats before anything else

            ArgumentParser argLoader = new ArgumentParser();
            List<ArgumentConfiguration> argsConfigs = new ArrayList<>();
            argsConfigs.add(new ArgumentConfiguration("debugMode", "--", false));

            ArgumentParser.loadArguments(argsConfigs);
            try {
                ArgumentParser.parseArguments(args);
            } catch (MissingArgumentException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            ConfigFile config = new ConfigFile(Configs.getStringConfiguration("configDir").getValue() + "configs.json");
            config.readFileContents();
            if(!config.isInitialized()) {
                config.setContents(Configs.configurations);
                config.saveFileContents();
            }else{
                Configs.setStartupConfigurations((HashMap)config.getMappedContents());
            }

            //Load up the api from the internal version
            CommandManager.commandManager = Optional.of((ICommandManager) CommandPool.INSTANCE);

            ServerConfigFile serverConfigFile = new ServerConfigFile();
            serverConfigFile.readFileContents();
            if(serverConfigFile.isInitialized()){
                Map<String, ServerConfiguration> serverConfigs = serverConfigFile.getMappedContents();
                for(String s : serverConfigs.keySet()) {
                    BotManager.createNewBot(serverConfigs.get(s));
                }
            }else{
                //Backup/Default server configuration file
                ServerConfiguration esper = new ServerConfiguration("Espernet", "irc.esper.net", "TCBot", "TEMP", Lists.newArrayList("Horfius"), "#TechnoDev", "#TheSixPack");
                serverConfigFile.addField(esper.getServerName(), esper);
                serverConfigFile.saveFileContents();

                BotManager.createNewBot(esper);
            }
//        BotManager.createNewBot("irc.technostarhosting.com", "#dev", "#TCBot");
            BotManager.start();//Launch bots

            if (ArgumentParser.getArgConfig("debugMode").asBoolean()) {
                BotManager.startDebugMonitor(); //TODO actual debug monitor
            }
        }catch (Exception e){
            e.printStackTrace();

            CrashLogBuilder b = new CrashLogBuilder(e);
            b.buildLog();

            System.exit(-1);
        }
    }
}
