package transaction;

import data.Customers;
import database.BlockedException;
import database.DatabaseUser;
import database.Databases;
import domain.Customer;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.util.ArrayList;

public class IsolationConcept extends IsolationConceptBase {
    protected String databaseName() {
        return Databases.Main;
    }

    @Test
    public void readUncommited() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_UNCOMMITTED, Databases.Main);
        final String newEmail = newEmail();
        you.updateCustomerEmail(Customers.AshokKumar, newEmail);
        Customer customer = i.getCustomer("Ashok Kumar");
        assert customer.getEmail().equals(newEmail);
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
    public void uploadOnSameRowBlocksNotEfficient() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, Databases.Main);
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        try {
            i.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException ignored) {
        }
    }

    @Test
    public void nonRepeatableRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, Databases.Main);
        Customer customer = i.getCustomer(Customers.AshokKumar);
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        you.commit();
        assert !customer.getEmail().equals(i.getCustomer(Customers.AshokKumar).getEmail());
    }

    @Test
    public void repeatableRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ, Databases.Main);
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
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ, Databases.Main);
        i.getCustomersHavingInEmail("bollywood");
        try {
            you.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException ignored) {
        }
    }

    @Test
    public void noLockEscalation() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ, Databases.Main);
        i.getCustomersHavingInEmail("thoughtworks");
        you.updateCustomerEmail("Dharmendra", newEmail());
    }

    @Test
    public void repeatableReadBlockedByOthersInsertsInefficient() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ, Databases.Main);
        i.getCustomersHavingInName("Ashok");
        you.createCustomer("Ashok Mitra", "amitra@yahoo.com");
        try {
            i.getCustomersHavingInName("Ashok");
            assert false;
        } catch (BlockedException ignored) {
        }
    }

    @Test
    public void phantomRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ, Databases.Main);
        ArrayList<Customer> customersList = i.getCustomersHavingInName("Ashok");
        you.createCustomer("Ashok Mitra", "amitra@yahoo.com");
        you.commit();
        ArrayList<Customer> newCustomersList = i.getCustomersHavingInName("Ashok");
        assert customersList.size() != newCustomersList.size();
    }

    @Test
    public void noPhantomRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_SERIALIZABLE, Databases.Main);
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
