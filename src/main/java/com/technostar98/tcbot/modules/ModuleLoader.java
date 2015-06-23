package com.technostar98.tcbot.modules;

import api.AssetLoader;
import com.technostar98.tcbot.lib.config.Configs;

import java.io.File;
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
            File[] modules = dir.listFiles();
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

                String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
                className = className.replace('/', '.');
                classes.add(classLoader.loadClass(className));
            }

            for(Class c : classes){
                Method[] methods = c.getMethods();//All of the methods for this class
                for(Method m : methods){
                    Annotation[] methodAnnotations = m.getDeclaredAnnotations();
                    for(Annotation a : methodAnnotations){
                        if(a instanceof AssetLoader){//If they are annotated with AssetLoader, they will be run
                            m.invoke(null);//It is assumed that the methods will be static and require no parameters
                        }
                    }
                }
            }

            Module module = null;
            api.Module modAnnotation = null;
            int index = 0;
            while((modAnnotation == null) && index < classes.size()){
                Class c = classes.get(index);
                if(modAnnotation == null) {
                    Annotation[] annotations = c.getAnnotations();
                    for (Annotation a : annotations) {
                        if (a instanceof api.Module) {
                            modAnnotation = (api.Module) a;
                            break;
                        }
                    }
                }
                index++;
            }

            return module;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
