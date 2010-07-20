package transaction;

import configuration.AltSqlServerSettings;
import configuration.AppConfigurationImpl;
import data.Customers;
import database.BlockedException;
import database.DatabaseUser;
import database.Databases;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;

public class IsolationConceptAlt extends IsolationConceptBase {
    @BeforeMethod
    public void setUp() throws Exception {
        you = createDatabaseUser();
    }

    private DatabaseUser createDatabaseUser() throws Exception {
        return new DatabaseUser(new AltSqlServerSettings(new AppConfigurationImpl()), Connection.TRANSACTION_READ_COMMITTED);
    }

    @Test
    public void readCommitedWhenSnapshotModeIsOff() throws Exception {
        i = createDatabaseUser();
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        try {
            i.getCustomer(Customers.AshokKumar);
            assert false;
        } catch (BlockedException ignored) {
        }
    }
}
