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
    private List<String> loadedModuleNames = new ArrayList<>();
    private HashMap<String, String> fileToLocalizedNames = new HashMap<>();
    private HashMap<String, Module> loadedModules = new HashMap<>();

    public List<String> getModuleNames(){
        return loadedModuleNames;
    }

    public boolean isModuleLoaded(String name){
        return loadedModuleNames.contains(name) || fileToLocalizedNames.containsKey(name);
    }

    public Module getModule(String name){
        return isModuleLoaded(name) ? loadedModules.get(name) == null ?
                loadedModules.get(fileToLocalizedNames.get(name)) : loadedModules.get(name) : null;
    }

    public List<Module> getModules(){
        return loadedModules.isEmpty() ? null : loadedModuleNames.parallelStream().map(s -> loadedModules.get(s)).collect(Collectors.toList());
    }

    public boolean loadModule(String name){
        if(isModuleLoaded(name))
            return true;
        else{
            ModuleLoader loader = new ModuleLoader(name);
            Module module = loader.loadModule();
//            System.out.println("Module " + module.getName() + " is " + (module == null ? "null" : "not null"));
//            if(module != null) System.out.println("Command count: " + module.getCommands().size() +
//                "\nFilter size: " + module.getFilters().size());
//            System.out.println("Putting key: " + name + "\tvalue: " + module.getName());
//            System.out.println("Loaded check: " + isModuleLoaded(name));

            if(module == null)
                return false;
            else {
                fileToLocalizedNames.put(name, module.getName());
                loadedModuleNames.add(module.getName());
                loadedModules.put(module.getName(), module);

                return true;
            }
        }
    }

    public void unloadModule(String name){
        if(isModuleLoaded(name)){
            loadedModules.remove(name);
            loadedModuleNames.remove(name);
        }
    }
}
