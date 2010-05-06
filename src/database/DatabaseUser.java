package database;

import configuration.AppConfigurationImpl;
import database.sqlserver.SqlServerConnection;

public class DatabaseUser {
    private SqlServerConnection connection;

    public DatabaseUser(int isolationLevel) throws Exception {
        connection = new SqlServerConnection(new AppConfigurationImpl(), isolationLevel);
        connection.beginTransaction();
    }

    public void updateCustomerEmail(String name, String email) throws Exception {
        connection.execute("update Customers set Email = ? where Name = ?", email, name);
    }

    public String getCustomerEmail(String name) throws Exception {
        Object[] customerValues = connection.queryValues("select Email from Customers where Name = ?", name);
        return (String) customerValues[0];
    }

    public void rollback() throws Exception {
        connection.rollback();
    }

    public void closeConnection() throws Exception {
        connection.close();
    }

    public void commit() throws Exception {
        connection.commit();
    }
}
