package com.technostar98.tcbot.modules;

import api.AssetLoader;
import com.google.common.collect.Lists;
import com.technostar98.tcbot.lib.config.Configs;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ModuleLoader {
    private final String name;

    public ModuleLoader(String moduleName){
        this.name = moduleName;
    }

    public Module loadModule(){
        try{
            File dir = new File(Configs.getStringConfiguration("moduleDir").getValue());
            File[] modules = dir.listFiles((d, n) -> n.endsWith(".jar"));
            boolean moduleExists = false;
            for(File f : modules)
                if(f.getName().contains(name)) moduleExists = true;

            if(!moduleExists) return null;

            String filePath = Configs.getStringConfiguration("moduleDir").getValue() + name + ".jar";
            JarFile jar = new JarFile(filePath);
            Enumeration e = jar.entries();
            ArrayList<Class<?>> classes = new ArrayList<>();

            URL[] urls = {new URL("jar:file:" + filePath + "!/")};
            URLClassLoader classLoader = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()){
                JarEntry jarEntry = (JarEntry) e.nextElement();
                if(jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) continue;

                String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6); //.class is 6 characters
                className = className.replace('/', '.');
                classes.add(classLoader.loadClass(className));
            }

            Module module = null;

            mLoop: for(Class c : classes){
                for(Annotation a : c.getDeclaredAnnotations()){
                    if(a instanceof api.Module){
                        module = new Module(((api.Module) a).name(), ((api.Module)a).id(), ((api.Module)a).version());
                        for(Method m : c.getDeclaredMethods()){
                            boolean toContinue = Lists.newArrayList(m.getDeclaredAnnotations()).stream().anyMatch(a2 -> a2 instanceof AssetLoader);
                            if(toContinue){
                                m.invoke(c);
                            }
                        }
                        break mLoop;
                    }
                }
            }

            return module;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
