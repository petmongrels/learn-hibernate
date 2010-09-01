package transaction;

import configuration.AppConfigurationImpl;
import configuration.OracleSettings;
import data.Customers;
import database.DatabaseUser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;

public class OracleIsolationScenario extends IsolationConceptBase {
    @BeforeMethod
    public void setUp() throws Exception {
        you = createDatabaseUser(Connection.TRANSACTION_SERIALIZABLE);
        i = createDatabaseUser(Connection.TRANSACTION_SERIALIZABLE);
    }

    private DatabaseUser createDatabaseUser(int isolationLevel) throws Exception {
        return new DatabaseUser(new OracleSettings(new AppConfigurationImpl()), isolationLevel);
    }

    @Test
    public void write_Possible_Even_When_Serializable_Lock_Held() throws Exception {
        i.getCustomer(Customers.AshokKumar);
        you.getCustomer(Customers.AshokKumar);
        i.updateCustomerEmail(Customers.AshokKumar, newEmail());
    }
}