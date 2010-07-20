package transaction;

import configuration.AppConfigurationImpl;
import configuration.SqlServerSettings;
import data.Customers;
import database.BlockedException;
import database.DatabaseUser;
import org.testng.annotations.Test;

import java.sql.Connection;

public class SqlServerIsolationScenario extends IsolationConceptBase {
    private DatabaseUser createDatabaseUser(int isolationLevel) throws Exception {
        return new DatabaseUser(new SqlServerSettings(new AppConfigurationImpl()), isolationLevel);
    }

    @Test
    public void pessimisticLock() throws Exception {
        you = createDatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        i = createDatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        i.getCustomer(Customers.AshokKumar);
        you.getCustomer(Customers.AshokKumar);
        try {
            i.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException ignored) {
        }
    }
}
