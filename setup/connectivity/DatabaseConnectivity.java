package connectivity;

import configuration.AppConfiguration;
import configuration.AppConfigurationImpl;
import database.Databases;
import database.sqlserver.SqlServerConnection;
import hibernate.OracleSessionFactoryWrapper;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.testng.annotations.Test;

public class DatabaseConnectivity {
    @Test
    public void canConnectToSqlServer() throws Exception {
        AppConfiguration configuration = new AppConfigurationImpl();
        SqlServerConnection connection = null;
        try {
            connection = new SqlServerConnection(configuration, Databases.Main);
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