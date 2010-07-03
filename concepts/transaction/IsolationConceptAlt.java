package transaction;

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
        you = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, databaseName());
    }    

    protected String databaseName() {
        return Databases.Alt;
    }

    @Test
    public void readCommitedWhenSnapshotModeIsOff() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, databaseName());
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        try {
            i.getCustomer(Customers.AshokKumar);
            assert false;
        } catch (BlockedException ignored) {
        }
    }
}
