package com.technostar98.tcbot.sql;

import java.sql.ResultSet;

public class MySQLConnector implements ISQLDatabaseConnector{

    @Override
    public boolean openConnection(String address) {
        return false;
    }

    @Override
    public boolean closeConnection() {
        return false;
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
