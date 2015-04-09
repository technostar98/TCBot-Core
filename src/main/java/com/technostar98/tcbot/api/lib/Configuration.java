package com.technostar98.tcbot.api.lib;

/**
 * Created by bret on 4/3/15.
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
