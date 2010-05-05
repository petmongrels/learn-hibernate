package connectivity;

import configuration.AppConfiguration;
import configuration.AppConfigurationImpl;
import org.testng.annotations.Test;
import database.sqlserver.SqlServerConnection;

public class DatabaseConnectivity {
    @Test
    public void CanConnectToSqlServer() throws Exception {
        AppConfiguration configuration = new AppConfigurationImpl();
        SqlServerConnection connection = null;
        try {
            connection = new SqlServerConnection(configuration);
        } finally {
            if (connection != null) connection.close();
        }
    }
}