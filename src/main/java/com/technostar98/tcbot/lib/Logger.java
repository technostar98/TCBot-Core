package com.technostar98.tcbot.lib;

import org.slf4j.LoggerFactory;

public class Logger {

    public static org.slf4j.Logger logger = LoggerFactory.getLogger(Logger.class);

    public static void info(String format, Object... args){
        logger.info(format, args);
    }

    public static void info(String message, Throwable t){
        logger.info(message, t);
    }

    public static void warning(String format, Object... args){
        logger.warn(format, args);
    }

    public static void warning(String message, Throwable t){
        logger.warn(message, t);
    }

    public static void debug(String format, Object... args){
        logger.debug(format, args);
    }

    public static void debug(String message, Throwable t){
        logger.debug(message, t);
    }

    public static void error(String format, Object... args){
        logger.error(format, args);
    }

    public static void error(String message, Throwable t){
        logger.error(message, t);
    }

    public static void trace(String format, Object... args){
        logger.trace(format, args);
    }

    public static void trace(String message, Throwable t){
        logger.trace(message, t);
    }
}
