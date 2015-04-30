package com.technostar98.tcbot.lib.exceptions;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
public class MissingArgumentException extends Exception{

    public MissingArgumentException(){
        super();
    }

    public MissingArgumentException(String message){
        super(message);
    }
}
