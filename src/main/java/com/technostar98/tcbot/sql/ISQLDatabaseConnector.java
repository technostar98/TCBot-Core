package com.technostar98.tcbot.sql;

import java.sql.ResultSet;

public interface ISQLDatabaseConnector {

    public boolean openConnection(String address);
    public boolean closeConnection();
    public boolean executeStatement(String sql);
    public boolean executeStatementMulti(String sql);
    public ResultSet executeQuery(String sql);
    public ResultSet executeQueryMulti(String sql);
}
