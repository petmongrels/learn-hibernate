package database;

import configuration.DatabaseSettings;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {
    private Connection connection;
    private DatabaseSettings databaseSettings;

    public DatabaseConnection(DatabaseSettings databaseSettings) throws Exception {
        this(databaseSettings, Connection.TRANSACTION_READ_COMMITTED);
    }

    public DatabaseConnection(DatabaseSettings databaseSettings, int isolationLevel) throws Exception {
        this.databaseSettings = databaseSettings;
        String connectionUrl = databaseSettings.url();
        Class.forName(databaseSettings.driverClass());
        connection = DriverManager.getConnection(connectionUrl, databaseSettings.user(), databaseSettings.password());
        connection.setTransactionIsolation(isolationLevel);
    }

    public void close() throws Exception {
        connection.close();
    }

    public Object[] queryValues(String sqlQuery, Object... parameters) throws Exception {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = preparedStatement(sqlQuery, parameters);
            resultSet = statement.executeQuery();
            resultSet.next();
            ArrayList list = getRow(resultSet);
            return list.toArray();
        } catch (SQLException e) {
            throw getException(e);
        } finally {
            DatabaseResource.closeSafely(statement, resultSet);
        }
    }

    private ArrayList getRow(ResultSet resultSet) {
        ArrayList<Object> list = new ArrayList<Object>(1);
        int resultIndex = 1;
        while (true) {
            try {
                Object value = resultSet.getObject(resultIndex++);
                list.add(value);
            } catch (SQLException e) {
                break;
            }
        }
        return list;
    }

    private PreparedStatement preparedStatement(String sqlQuery, Object[] parameters) throws SQLException {
        PreparedStatement statement = preparedStatement(sqlQuery);
        for (int i = 1; i <= parameters.length; i++) {
            statement.setObject(i, parameters[i - 1]);
        }
        return statement;
    }

    public ArrayList<Object[]> queryRows(String sqlQuery, Object... parameters) throws Exception {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = preparedStatement(sqlQuery, parameters);
            resultSet = statement.executeQuery();
            ArrayList<Object[]> rows = new ArrayList<Object[]>();
            while (resultSet.next()) {
                ArrayList list = getRow(resultSet);
                rows.add(list.toArray());
            }
            return rows;
        } catch (SQLException e) {
            throw getException(e);
        } finally {
            DatabaseResource.closeSafely(statement, resultSet);
        }
    }

    public ArrayList<Object[]> queryRows(String sqlQuery) throws Exception {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);
            ArrayList<Object[]> rows = new ArrayList<Object[]>();
            while (resultSet.next()) {
                ArrayList list = getRow(resultSet);
                rows.add(list.toArray());
            }
            return rows;
        } catch (SQLException e) {
            throw getException(e);
        } finally {
            DatabaseResource.closeSafely(statement, resultSet);
        }
    }

    private PreparedStatement preparedStatement(String sqlQuery) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setQueryTimeout(databaseSettings.queryTimeOut());
        return statement;
    }

    public int execute(String sql, Object... parameters) throws Exception {
        PreparedStatement statement = null;
        try {
            statement = preparedStatement(sql);
            statement.setQueryTimeout(5);
            for (int i = 1; i <= parameters.length; i++) {
                statement.setObject(i, parameters[i - 1]);
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw getException(e);
        } finally {
            DatabaseResource.closeSafely(statement);
        }
    }

    private Exception getException(SQLException e) throws SQLException {
        if (e.getMessage().equals("The query has timed out.")) return new BlockedException(e);
        return e;
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