package transaction;

import configuration.AppConfigurationImpl;
import configuration.OracleSettings;
import data.Customers;
import database.DatabaseUser;
import database.Databases;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;

public class OracleIsolationScenario extends IsolationConceptBase {
    protected String databaseName() {
        return Databases.Main;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        you = createDatabaseUser(Connection.TRANSACTION_SERIALIZABLE);
        i = createDatabaseUser(Connection.TRANSACTION_SERIALIZABLE);
    }

    private DatabaseUser createDatabaseUser(int isolationLevel) throws Exception {
        return new DatabaseUser(new OracleSettings(new AppConfigurationImpl()), isolationLevel);
    }

    @Test
    public void writePossibleEvenWhenSerializableLockHeld() throws Exception {
        i.getCustomer(Customers.AshokKumar);
        you.getCustomer(Customers.AshokKumar);
        i.updateCustomerEmail(Customers.AshokKumar, newEmail());
    }
}