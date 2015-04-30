package com.technostar98.tcbot.api.lib;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 * @param <U> Value type of Configuration
 */
public class Configuration<U> {
    private U value;
    private final Class<?> type; //Possibly useful for later
    private String name;

    public Configuration(String name, U val, Class<?> type){
        this.value = val;
        this.name = name;
        this.type = type;
    }

    public U getValue(){
        return value;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public String getTypeName(){
        return this.type.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(U value) {
        this.value = value;
    }
}
