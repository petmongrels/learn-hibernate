package transaction;

import configuration.AppConfigurationImpl;
import configuration.SqlServerSettings;
import data.Customers;
import database.BlockedException;
import database.DatabaseUser;
import domain.Customer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;

public class SqlServerIsolationConcept extends IsolationConceptBase {
    @BeforeMethod
    public void setUp() throws Exception {
        you = createDatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
    }

    private DatabaseUser createDatabaseUser(int isolationLevel) throws Exception {
        return new DatabaseUser(new SqlServerSettings(new AppConfigurationImpl()), isolationLevel);
    }

    @Test
    public void readUncommited() throws Exception {
        i = createDatabaseUser(Connection.TRANSACTION_READ_UNCOMMITTED);
        final String newEmail = newEmail();
        you.updateCustomerEmail(Customers.AshokKumar, newEmail);
        Customer customer = i.getCustomer("Ashok Kumar");
        assert customer.getEmail().equals(newEmail);
    }

    @Test
    public void repeatableRead() throws Exception {
        i = createDatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        Customer customer = i.getCustomer(Customers.AshokKumar);
        try {
            you.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException e) {
            assert customer.getEmail().equals(i.getCustomer(Customers.AshokKumar).getEmail());
        }
    }

    @Test
    public void lockEscalation() throws Exception {
        i = createDatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        i.getCustomersHavingInEmail("bollywood");
        try {
            you.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException ignored) {
        }
    }

    @Test
    public void noLockEscalation() throws Exception {
        i = createDatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        i.getCustomersHavingInEmail("thoughtworks");
        you.updateCustomerEmail("Dharmendra", newEmail());
    }

    @Test
    public void repeatableReadBlockedByOthersInsertsInefficient() throws Exception {
        i = createDatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        i.getCustomersHavingInName("Ashok");
        you.createCustomer("Kishore Kumar", "kkumar@yahoo.com");
        try {
            i.getCustomersHavingInName("Ashok");
            assert false;
        } catch (BlockedException ignored) {
        }
    }

    @Test
    public void phantomRead() throws Exception {
        final UUID uuid = UUID.randomUUID();
        i = createDatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        ArrayList<Customer> customersList = i.getCustomersHavingInName("Ashok");
        you.createCustomer("Ashok Mitra", "amitra" + uuid.toString() + "@yahoo.com");
        you.commit();
        ArrayList<Customer> newCustomersList = i.getCustomersHavingInName("Ashok");
        assert customersList.size() + 1 == newCustomersList.size();
    }

    @Test
    public void noPhantomRead() throws Exception {
        i = createDatabaseUser(Connection.TRANSACTION_SERIALIZABLE);
        ArrayList<Customer> customersList = i.getCustomersHavingInName("Ashok");
        try {
            you.createCustomer("Ashok Mitra", "amitra@yahoo.com");
            assert false;
        } catch (BlockedException e) {
            ArrayList<Customer> newCustomersList = i.getCustomersHavingInName("Ashok");
            assert customersList.size() == newCustomersList.size();
        }
    }    
}
