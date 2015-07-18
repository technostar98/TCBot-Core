package com.technostar98.tcbot.lib;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public class Value {
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public enum Type{
        INTEGER(Integer.class), STRING(String.class), FLOAT(Float.class), ;

        public final Class parent;

        Type(Class parent){
            this.parent = parent;
        }
    }

    public final Type type;
    private Object value;

    public Value(Type type, Object val){
        this.type = type;
        this.value = val;
    }
}
