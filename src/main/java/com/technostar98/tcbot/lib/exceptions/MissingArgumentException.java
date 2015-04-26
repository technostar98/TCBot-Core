package com.technostar98.tcbot.lib.exceptions;

public class MissingArgumentException extends Exception{

    public MissingArgumentException(){
        super();
    }

    public MissingArgumentException(String message){
        super(message);
    }
}
