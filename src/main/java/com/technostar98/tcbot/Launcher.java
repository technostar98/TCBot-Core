package com.technostar98.tcbot;

import api.command.CommandManager;
import api.command.ICommandManager;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.io.ConfigFile;
import com.technostar98.tcbot.io.ServerConfigFile;
import com.technostar98.tcbot.lib.ArgumentParser;
import com.technostar98.tcbot.lib.CrashLogBuilder;
import com.technostar98.tcbot.lib.config.Configs;
import com.technostar98.tcbot.lib.config.ServerConfiguration;
import com.technostar98.tcbot.lib.config.Stats;
import com.technostar98.tcbot.modules.CommandPool;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;


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

        try {
            Stats.getStat("startTime").getValue();//Make sure <cinit> is called in stats before anything else

            StringJoiner joiner = new StringJoiner(" ");
            for(String arg : args) joiner.add(arg);
            ArgumentParser.init(joiner.toString());

            ArgumentParser argLoader = ArgumentParser.INSTANCE();
            argLoader.registerOptionalArgument("debug", ArgumentParser.EQUALS | ArgumentParser.COLON | ArgumentParser.SPACED);
            argLoader.registerOptionalArgument("maxScriptThreads", ArgumentParser.COLLECTION_MAP | ArgumentParser.COLON | ArgumentParser.EQUALS);
            argLoader.registerOptionalArgument("noGui", ArgumentParser.EQUALS | ArgumentParser.COLON | ArgumentParser.SPACED);

            //EXAMPLE CODE
            /*argLoader.registerOptionalArgument("test1", ArgumentParser.EQUALS | ArgumentParser.COLON);
            argLoader.registerOptionalArgument("test2", ArgumentParser.COLLECTION_LIST);
            argLoader.registerOptionalArgument("test3", ArgumentParser.COLLECTION_MAP);*/

            argLoader.processArguments();

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
                ServerConfiguration esper = new ServerConfiguration("Espernet", "irc.esper.net", "TCBot", "TEMP", Lists.newArrayList("Horfius"), "#TechnoDev");
                serverConfigFile.addField(esper.getServerName(), esper);
                serverConfigFile.saveFileContents();

                BotManager.createNewBot(esper);
            }

            BotManager.start();//Launch bots

            if (Boolean.valueOf((String)argLoader.getArgumentValue("debug"))) {
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
