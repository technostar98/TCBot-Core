package com.technostar98.tcbot.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static List<String> loadedModuleNames = new ArrayList<>();
    private static HashMap<String, String> fileToLocalizedNames = new HashMap<>();
    private static HashMap<String, Module> loadedModules = new HashMap<>();

    public static List<String> getModuleNames(){
        return loadedModuleNames;
    }

    public static boolean isModuleLoaded(String name){
        return loadedModuleNames.contains(name) || fileToLocalizedNames.containsKey(name);
    }

    public static Module getModule(String name){
        return isModuleLoaded(name) ? loadedModules.get(name) == null ?
                loadedModules.get(fileToLocalizedNames.get(name)) : loadedModules.get(name) : null;
    }

    public static List<Module> getModules(){
        return loadedModules.isEmpty() ? null : loadedModuleNames.parallelStream().map(s -> loadedModules.get(s)).collect(Collectors.toList());
    }

    public static boolean loadModule(String name){
        if(isModuleLoaded(name))
            return true;
        else{
            ModuleLoader loader = new ModuleLoader(name);
            Module module = loader.loadModule();
            System.out.println("Module " + module.getName() + " is " + (module == null ? "null" : "not null"));
            if(module != null) System.out.println("Command count: " + module.getCommands().size() +
                "\nFilter size: " + module.getFilters().size());
            System.out.println("Putting key: " + name + "\tvalue: " + module.getName());
            fileToLocalizedNames.put(name, module.getName());
            loadedModuleNames.add(module.getName());
            loadedModules.put(module.getName(), module);
            System.out.println("Loaded check: " + isModuleLoaded(name));

            if(module == null)
                return false;
            else
                return true;
        }
    }

    public static void unloadModule(String name){
        if(isModuleLoaded(name)){
            loadedModules.remove(name);
            loadedModuleNames.remove(name);
        }
    }
}
