package transaction;

import configuration.AppConfigurationImpl;
import data.Customers;
import database.BlockedException;
import database.DatabaseSettingsFactory;
import database.DatabaseUser;
import database.Databases;
import domain.Customer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;

public class IsolationConcept extends IsolationConceptBase {
    @BeforeMethod
    public void setUp() throws Exception {
        you = new DatabaseUser(Databases.Main, Connection.TRANSACTION_READ_COMMITTED);
    }

    @Test
    public void readCommited() throws Exception {
        i = new DatabaseUser(Databases.Main, Connection.TRANSACTION_READ_COMMITTED);
        final String newEmail = newEmail();
        you.updateCustomerEmail(Customers.AshokKumar, newEmail);
        Customer customer = i.getCustomer(Customers.AshokKumar);
        assert !customer.getEmail().equals(newEmail);

        assert newEmail.equals(you.getCustomer(Customers.AshokKumar).getEmail());
    }

    @Test
    public void nonRepeatableRead() throws Exception {
        i = new DatabaseUser(Databases.Main, Connection.TRANSACTION_READ_COMMITTED);
        Customer customer = i.getCustomer(Customers.AshokKumar);
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        you.commit();
        assert !customer.getEmail().equals(i.getCustomer(Customers.AshokKumar).getEmail());
    }

    @Test
    public void updateOnSameRowBlocks() throws Exception {
        i = createDatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        try {
            i.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException ignored) {
        }
    }

    private DatabaseUser createDatabaseUser(int isolationLevel) throws Exception {
        return new DatabaseUser(DatabaseSettingsFactory.create(new AppConfigurationImpl()), isolationLevel);
    }
}
