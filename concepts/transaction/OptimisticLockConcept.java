package transaction;

import data.Customers;
import database.DatabaseUser;
import domain.Customer;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;

public class OptimisticLockConcept {
    private DatabaseUser you;
    private DatabaseUser i;

    @BeforeTest
    public void setUp() throws Exception {
        you = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
    }

    @AfterTest
    public void tearDown() throws Exception {
        you.rollback();
        you.closeConnection();
        i.rollback();
        i.closeConnection();
    }

    @Test
    public void updateLock() throws Exception {
        Customer myCustomer = you.getCustomer(Customers.AshokKumar);
        Customer yourCustomer = you.getCustomer(Customers.AshokKumar);
        int count = i.updateCustomerEmail(Customers.AshokKumar, myCustomer.getEmail(), myCustomer.getVersion());
        assert count == 1;
        i.commit();
        count = you.updateCustomerEmail(Customers.AshokKumar, myCustomer.getEmail(), yourCustomer.getVersion());
        assert count == 0;
    }
}
