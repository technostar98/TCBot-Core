package com.technostar98.tcbot.modules;

import api.command.Command;
import api.command.CommandManager;
import api.command.ICommandManager;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.technostar98.tcbot.bot.BotManager;
import com.technostar98.tcbot.command.ChannelManager;
import com.technostar98.tcbot.lib.Logger;
import com.technostar98.tcbot.lib.config.Configs;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ModuleManager {
    protected LoadingCache<String, Module> modules = CacheBuilder.<Integer, Module>newBuilder()
            .maximumSize(256) //I hope we never reach this many modules because I have no clue how the bot would perform
            .weakValues() //Garbage collect modules no longer referenced anywhere
            .removalListener(i -> { //To make sure all references to the module are removed if forcefully removed
                Logger.warning("Unloading module ...");
                ICommandManager manager = CommandManager.commandManager.get();
                Preconditions.checkNotNull(manager);

                Module m = (Module) i.getValue();
                Logger.warning("Unloading module %s, v.%d", m.getName(), m.getVersion());
                Optional<Map<String, List<String>>> chansO = manager.getChannelsWithModule(m.getID());
                if (chansO.isPresent()) {
                    Map<String, List<String>> chans = chansO.get();
                    List<String> commandIDs = m.getCommands();
                    List<String> filterIDs = m.getFilters();

                    chans.keySet().forEach(s -> {
                                List<String> channels = chans.get(s);
                                channels.forEach(c -> {
                                    ChannelManager channelManager = BotManager.getChannelManager(s, c).get();
                                    channelManager.removeRawModule(m.getID());
                                    commandIDs.forEach(d -> channelManager.removeCommand(m.getID() + ":" + d));
                                    filterIDs.forEach(d -> channelManager.removeFilter(m.getID() + ":" + d));
                                });
                            }
                    );
                    Logger.info("Removed module %s from all channels in all servers.", m.getName());
                } else {
                    Logger.info("Module %s was not in any channels, fully removed.", m.getName());
                }
            })
            .build(new CacheLoader<String, Module>() {
                @Override
                public Module load(String key) throws Exception {
                    return new ModuleLoader(key).loadModule();
                }
            });
    protected BiMap<String, String> idsToNames = HashBiMap.create();

    /**
     * Retrieve a module, will load if it is not loaded already.
     * @param id
     * @return
     */
    public Optional<Module> getModule(String id) {
        Optional<Module> module = Optional.absent();
        try {
            module = Optional.fromNullable(modules.get(id));
        } catch (Exception e) {
            Logger.error("Could not load up module from " + id, e);
        }

        if(module.isPresent()){
            idsToNames.put(module.get().getID(), id);
        }

        return module;
    }

    public void refreshModule(String id){
        modules.invalidate(id);
        modules.cleanUp();
        try {
            modules.get(id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
