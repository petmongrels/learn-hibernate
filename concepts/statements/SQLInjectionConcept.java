package statements;

import configuration.AppConfigurationImpl;
import configuration.DatabaseSettings;
import database.DatabaseConnection;
import database.DatabaseSettingsFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class SQLInjectionConcept {
    private DatabaseConnection connection;

    @BeforeMethod
    public void setUp() throws Exception {
        final DatabaseSettings databaseSettings = DatabaseSettingsFactory.create(new AppConfigurationImpl());
        connection = new DatabaseConnection(databaseSettings);
        connection.beginTransaction();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        connection.rollback();
        connection.close();
    }

    @Test
    public void executeUsingCreatedStatement() throws Exception {
        ArrayList<Object[]> customers = connection.queryRows("select Id from Customers where Name = '" + getName() + "'");
        assert customers.size() == 1;
        ArrayList<Object[]> transactions = connection.queryRows("select Id from Transactions");
        assert transactions.size() == 0;
    }

    private String getName() {
        return "Ashok Kumar';delete from Transactions--";
    }

    @Test
    public void executeUsingPreparedStatement() throws Exception {
        ArrayList<Object[]> customers = connection.queryRows("select Id from Customers where Name = ?", getName());
        assert customers.size() == 0;
        ArrayList<Object[]> transactions = connection.queryRows("select Id from Transactions");
        assert transactions.size() != 0;
    }
}
