package transaction;

import data.Customers;
import database.BlockedException;
import database.DatabaseUser;
import database.Databases;
import domain.Customer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class OracleIsolationConcept extends IsolationConceptBase {
    @BeforeMethod
    public void setUp() throws Exception {
        you = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, databaseName());
    }

    protected String databaseName() {
        return Databases.Main;
    }

    @Test
    public void cannotReadUncommitedData() throws Exception {
        try {
            i = new DatabaseUser(Connection.TRANSACTION_READ_UNCOMMITTED, Databases.Main);
            assert false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void repeatableRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_SERIALIZABLE, Databases.Main);
        Customer customer = i.getCustomer(Customers.AshokKumar);
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        assert customer.getEmail().equals(i.getCustomer(Customers.AshokKumar).getEmail());
    }

    @Test
    public void phantomRead() throws Exception {
        final UUID uuid = UUID.randomUUID();
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, Databases.Main);
        ArrayList<Customer> customersList = i.getCustomersHavingInName("Ashok");
        you.createCustomer("Ashok Mitra", "amitra" + uuid.toString() + "@yahoo.com");
        you.commit();
        ArrayList<Customer> newCustomersList = i.getCustomersHavingInName("Ashok");
        assert customersList.size() + 1 == newCustomersList.size();
    }

    @Test
    public void repeatableReadNotBlockedByOthersInserts() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_SERIALIZABLE, Databases.Main);
        i.getCustomersHavingInName("Ashok");
        you.createCustomer("Kishore Kumar", "kkumar@yahoo.com");
        i.getCustomersHavingInName("Ashok");
    }

    @Test
    public void noPhantomEvenWhenTheDatabaseHasChanged() throws Exception {
        final UUID uuid = UUID.randomUUID();
        i = new DatabaseUser(Connection.TRANSACTION_SERIALIZABLE, Databases.Main);
        ArrayList<Customer> customersList = i.getCustomersHavingInName("Ashok");
        you.createCustomer("Ashok Mitra", "amitra" + uuid.toString() + "@yahoo.com");
        you.commit();
        ArrayList<Customer> newCustomersList = i.getCustomersHavingInName("Ashok");
        assert customersList.size() == newCustomersList.size();
    }

    @Test
    public void updateOnSameRowBlocks() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, Databases.Main);
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        try {
            i.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException ignored) {
        }
    }
}