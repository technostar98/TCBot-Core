package com.technostar98.tcbot.sql;

import com.technostar98.tcbot.lib.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteJDBC implements ISQLDatabaseConnector{
    private Connection connection = null;

    protected SQLiteJDBC(){

    }

    @Override
    public boolean openConnection(String address) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(address);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        Logger.info("Successfully connected to database: " + address);

        return false;
    }

    @Override
    public boolean closeConnection() {
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean executeStatement(String sql) {
        return false;
    }

    @Override
    public boolean executeStatementMulti(String sql) {
        return false;
    }

    @Override
    public ResultSet executeQuery(String sql) {
        return null;
    }

    @Override
    public ResultSet executeQueryMulti(String sql) {
        return null;
    }
}
