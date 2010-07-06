package transaction;

import data.Customers;
import database.DatabaseUser;
import database.Databases;
import domain.Customer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;

public class IsolationConcept extends IsolationConceptBase {
    @BeforeMethod
    public void setUp() throws Exception {
        you = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, databaseName());
    }

    protected String databaseName() {
        return Databases.Main;
    }

    @Test
    public void readCommited() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, Databases.Main);
        final String newEmail = newEmail();
        you.updateCustomerEmail(Customers.AshokKumar, newEmail);
        Customer customer = i.getCustomer(Customers.AshokKumar);
        assert !customer.getEmail().equals(newEmail);

        assert newEmail.equals(you.getCustomer(Customers.AshokKumar).getEmail());
    }

    @Test
    public void nonRepeatableRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, Databases.Main);
        Customer customer = i.getCustomer(Customers.AshokKumar);
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        you.commit();
        assert !customer.getEmail().equals(i.getCustomer(Customers.AshokKumar).getEmail());
    }
}
