package transaction;

import configuration.AppConfigurationImpl;
import configuration.SqlServerSettings;
import data.Customers;
import database.BlockedException;
import database.DatabaseUser;
import database.Databases;
import org.testng.annotations.Test;

import java.sql.Connection;

public class SqlServerIsolationScenario extends IsolationConceptBase {
    protected String databaseName() {
        return Databases.Main;
    }

    private DatabaseUser createDatabaseUser(int isolationLevel) throws Exception {
        return new DatabaseUser(new SqlServerSettings(new AppConfigurationImpl()), isolationLevel);
    }

    @Test
    public void noOneCanWriteWhenRepeatableReadOrHigherLockAcquired() throws Exception {
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
