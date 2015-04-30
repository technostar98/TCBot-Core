package com.technostar98.tcbot.sql;

import java.sql.ResultSet;

/**
 * <p>Created by Bret 'Horfius' Dusseault in 2015.
 * All code in this file is open-source and
 * may be used with permission of Bret Dusseault.
 * Any violations of this violate the terms of
 * the license of TCBot-Core.</p>
 *
 * @author Bret 'Horfius' Dusseault
 */
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
