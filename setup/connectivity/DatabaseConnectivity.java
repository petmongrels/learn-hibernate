package connectivity;

import configuration.AppConfiguration;
import configuration.AppConfigurationImpl;
import configuration.DatabaseSettings;
import database.DatabaseSettingsFactory;
import database.Databases;
import database.DatabaseConnection;
import hibernate.OracleSessionFactoryWrapper;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.testng.annotations.Test;

public class DatabaseConnectivity {
    @Test
    public void canConnectToSqlServer() throws Exception {
        AppConfiguration configuration = new AppConfigurationImpl();
        DatabaseConnection connection = null;
        try {
            final DatabaseSettings databaseSettings = DatabaseSettingsFactory.create(configuration, Databases.Main, "sqlserver");
            connection = new DatabaseConnection(databaseSettings);
        } finally {
            if (connection != null) connection.close();
        }
    }

    @Test
    public void canConnectToOracle() throws Exception {
        SessionFactory sessionFactory = null;
        Session session = null;
        try {
            sessionFactory = new OracleSessionFactoryWrapper().getSessionFactory();
            assert sessionFactory != null;
            session = sessionFactory.openSession();
            assert session != null;
        } finally {
            assert session != null;
            session.close();
            sessionFactory.close();
        }
    }
}