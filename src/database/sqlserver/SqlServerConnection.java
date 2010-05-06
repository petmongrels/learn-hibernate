package database.sqlserver;

import configuration.AppConfiguration;
import database.DatabaseResource;
import net.sourceforge.jtds.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;

public class SqlServerConnection {
    private Connection connection;
    private AppConfiguration configuration;

    public SqlServerConnection(AppConfiguration configuration) throws Exception {
        this(configuration, Connection.TRANSACTION_READ_COMMITTED);
    }

    public SqlServerConnection(AppConfiguration configuration, int isolationLevel) throws Exception {
        this.configuration = configuration;
        String connectionUrl = "jdbc:jtds:sqlserver://viveksingh:1433/LearnHibernate;SelectMethod=cursor";
        Class.forName(Driver.class.getName());
        connection = DriverManager.getConnection(connectionUrl, configuration.sqlServerUser(), configuration.sqlServerPassword());
        connection.setTransactionIsolation(isolationLevel);
    }

    public void close() throws Exception {
        connection.close();
    }

    public Object[] queryValues(String sqlQuery, Object ... parameters) throws Exception {
        PreparedStatement statement = preparedStatement(sqlQuery);
        for (int i = 1; i <= parameters.length; i++) {
            statement.setObject(i, parameters[i - 1]);
        }
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        ArrayList list = new ArrayList(1);
        int resultIndex = 1;
        while (true) {
            try {
                Object value = resultSet.getObject(resultIndex++);
                list.add(value);
            } catch (SQLException e) {
                break;
            }
        }
        DatabaseResource.closeSafely(statement, resultSet);
        return list.toArray();
    }

    private PreparedStatement preparedStatement(String sqlQuery) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setQueryTimeout(configuration.queryTimeoutInSeconds());
        return statement;
    }

    public void execute(String sql, Object ... parameters) throws Exception {
        PreparedStatement statement = preparedStatement(sql);
        statement.setQueryTimeout(5);
        for (int i = 1; i <= parameters.length; i++) {
            statement.setObject(i, parameters[i - 1]);
        }
        statement.executeUpdate();
        DatabaseResource.closeSafely(statement);
    }

    public void beginTransaction() throws Exception {
        connection.setAutoCommit(false);
    }

    public void rollback() throws Exception {
        connection.rollback();
    }

    public void commit() throws Exception {
        connection.commit();
    }
}
