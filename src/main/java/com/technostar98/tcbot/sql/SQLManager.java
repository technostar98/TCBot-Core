package com.technostar98.tcbot.sql;

import com.technostar98.tcbot.lib.Logger;

import java.sql.ResultSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SQLManager {
    private static ISQLDatabaseConnector connector = null;
    private static Executor threadPool = Executors.newFixedThreadPool(8);

    public static ISQLDatabaseConnector getConnector(){
        return connector;
    }

    public static void setConnector(ISQLDatabaseConnector newConnector, String connection){
        if(connector != null) {
            if (connector.closeConnection()) {
                connector = newConnector;
                if (!connector.openConnection(connection)) {
                    Logger.error("Could not open connection to database " + connection);
                }
            } else{
                Logger.error("Could not close connection to database " + connector.getClass().getName());
            }
        }else{
            connector = newConnector;
            if(!connector.openConnection(connection)){
                Logger.error("Could not connect to database " + connection);
            }
        }
    }

    public static void executeStatement(String sql){
        threadPool.execute(() -> connector.executeStatement(sql));
    }

    public static ResultSet executeQuery(String sql){
        return connector.executeQuery(sql);
    }

    public static void close(){
        connector.closeConnection();
    }
}
