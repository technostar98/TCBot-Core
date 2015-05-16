package com.technostar98.tcbot.api.lib;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class ValuePair<V1, V2> {
    private V1 value1;
    private V2 value2;

    public ValuePair(V1 value1, V2 value2){
        this.value1 = value1;
        this.value2 = value2;
    }

    public V1 getValue1() {
        return value1;
    }

    public void setValue1(V1 value1) {
        this.value1 = value1;
    }

    public V2 getValue2() {
        return value2;
    }

    public void setValue2(V2 value2) {
        this.value2 = value2;
    }
}
