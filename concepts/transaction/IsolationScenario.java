package transaction;

import data.Customers;
import database.BlockedException;
import database.DatabaseUser;
import database.Databases;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;

public class IsolationScenario extends IsolationConceptBase {
    protected String databaseName() {
        return Databases.Main;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        you = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ, databaseName());
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ, databaseName());
    }

    @Test
    public void pessimisticLock() throws Exception {
        i.getCustomer(Customers.AshokKumar);
        you.getCustomer(Customers.AshokKumar);
        try {
            i.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException ignored) {
        }
    }
}
