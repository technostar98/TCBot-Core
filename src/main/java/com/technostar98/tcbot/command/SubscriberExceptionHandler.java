package com.technostar98.tcbot.command;

/**
 * @author Bret 'Horfius' Dusseault
 * @version 1.0
 */
public interface SubscriberExceptionHandler {
    public void handleException(Throwable throwable, SubscriberExceptionContext context);
}
