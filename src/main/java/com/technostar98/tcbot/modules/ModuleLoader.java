package com.technostar98.tcbot.modules;

import com.technostar98.tcbot.api.AssetLoader;
import com.technostar98.tcbot.api.Commands;
import com.technostar98.tcbot.api.Filters;
import com.technostar98.tcbot.api.command.Command;
import com.technostar98.tcbot.api.filter.ChatFilter;
import com.technostar98.tcbot.lib.config.Configs;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
            com.technostar98.tcbot.api.Module modAnnotation = null;
            List<Command> commands = null;
            List<ChatFilter> filters = null;
            int index = 0;
            while((modAnnotation == null || commands == null || filters == null) && index < classes.size()){
                Class c = classes.get(index);
                if(modAnnotation == null) {
                    Annotation[] annotations = c.getAnnotations();
                    for (Annotation a : annotations) {
                        if (a instanceof com.technostar98.tcbot.api.Module) {
                            modAnnotation = (com.technostar98.tcbot.api.Module) a;
                            break;
                        }
                    }
                }

                if(commands == null || filters == null) {
                    Field[] fields = c.getFields();//All the fields
                    for (Field f : fields) {
                        if (f.getType().equals(List.class) || f.getType().equals(ArrayList.class)
                                || f.getType().equals(LinkedList.class)) {//We are only looking for a child of List
                            Annotation[] methodAnnotations = f.getDeclaredAnnotations();
                            for (Annotation a : methodAnnotations) {
                                if (commands == null && a instanceof Commands) {//Load the commands
                                    commands = (List<Command>) f.get(null);//Anything annotated by Commands is assumed static
                                } else if (filters == null && a instanceof Filters) {//Load the filters
                                    filters = (List<ChatFilter>) f.get(null);//Anything annotated by Filters is assumed static
                                }
                            }
                        }
                    }
                }
                index++;
            }

            if(modAnnotation != null){
                if(commands == null) commands = new ArrayList<>();
                if(filters == null) filters = new ArrayList<>();

                module = new Module(modAnnotation.name(), modAnnotation.id(), modAnnotation.version(),
                        commands, filters);
            }

            return module;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
