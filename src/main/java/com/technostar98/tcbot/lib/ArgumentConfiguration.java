package com.technostar98.tcbot.lib;

public class ArgumentConfiguration {
    private final String argName, prefix;
    private Object value = null;
    private final boolean necessary;
    private boolean fulfilled = false;

    public ArgumentConfiguration(String argName, String prefix, boolean necessary){
        this.argName = argName;
        this.prefix = prefix;
        this.necessary = necessary;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public String getArgName() {
        return argName;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public boolean isNecessary() {
        return necessary;
    }

    public Object value() {
        return value;
    }

    public String asString(){
        return (String) value;
    }

    public char asChar(){
        return (Character) value;
    }

    public byte asByte(){
        return Byte.valueOf(asString());
    }

    public short asShort(){
        return Short.valueOf(asString());
    }

    public int asInt(){
        return Integer.valueOf(asString());
    }

    public long asLong(){
        return Long.valueOf(asString());
    }

    public float asFloat(){
        return Float.valueOf(asString());
    }

    public double asDouble(){
        return Double.valueOf(asString());
    }

    public boolean asBoolean(){
        return Boolean.valueOf(asString());
    }
}
