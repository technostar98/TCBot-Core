package com.technostar98.tcbot.modules;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    protected BiMap<String, Integer> moduleIDs = HashBiMap.create();
    protected Map<Integer, Module> modules = Maps.newHashMap();
    protected BiMap<String, String> moduleIDtoFiles = HashBiMap.create();

    public void loadModule(String key){
        if(modules.containsKey(moduleIDs.get(key))){

        }
    }

    public Module getModule(String name) {
        if(moduleIDs.containsKey(name)){
            return  modules.get(moduleIDs.get(name));
        }else if(modules.keySet().stream().anyMatch(i -> modules.get(i).getName().equals(name))){
            return modules.keySet().stream().filter(i -> modules.get(i).getName().equals(name)).map(i -> modules.get(i)).collect(Collectors.toList()).get(0);
        }

        return null;
    }
}
