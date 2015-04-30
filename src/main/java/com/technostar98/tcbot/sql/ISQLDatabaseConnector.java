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
public interface ISQLDatabaseConnector {

    public boolean openConnection(String address);
    public boolean closeConnection();
    public boolean executeStatement(String sql);
    public boolean executeStatementMulti(String sql);
    public ResultSet executeQuery(String sql);
    public ResultSet executeQueryMulti(String sql);
}
