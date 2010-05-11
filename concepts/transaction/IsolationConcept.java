package transaction;

import data.Customers;
import database.BlockedException;
import database.DatabaseUser;
import domain.Customer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

public class IsolationConcept {
    private DatabaseUser you;
    private DatabaseUser i;

    @BeforeMethod
    public void setUp() throws Exception {
        you = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        try {
            you.rollback();
            you.closeConnection();
            i.rollback();
            i.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readUncommited() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_UNCOMMITTED);
        final String newEmail = newEmail();
        you.updateCustomerEmail(Customers.AshokKumar, newEmail);
        Customer customer = i.getCustomer("Ashok Kumar");
        assert customer.getEmail().equals(newEmail);
    }

//    @Test
    public void readCommited_WhenSnapshotModeIsOff() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
        final String newEmail = newEmail();
        you.updateCustomerEmail(Customers.AshokKumar, newEmail);
        try {
            i.getCustomer(Customers.AshokKumar);
            assert false;
        } catch (BlockedException ignored) {
        }
    }

    @Test
    public void readCommited() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
        final String newEmail = newEmail();
        you.updateCustomerEmail(Customers.AshokKumar, newEmail);
        Customer customer = i.getCustomer(Customers.AshokKumar);
        assert !customer.getEmail().equals(newEmail);

        assert newEmail.equals(you.getCustomer(Customers.AshokKumar).getEmail());
    }

    @Test
    public void uploadOnSameRowBlocksNotEfficient() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        try {
            i.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException ignored) {
        }
    }

    @Test
    public void nonRepeatableRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
        Customer customer = i.getCustomer(Customers.AshokKumar);
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        you.commit();
        assert !customer.getEmail().equals(i.getCustomer(Customers.AshokKumar).getEmail());
    }

    @Test
    public void repeatableRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
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
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        i.getCustomersHavingInEmail("bollywood");
        try {
            you.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException ignored) {
        }
    }

    @Test
    public void noLockEscalation() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        i.getCustomersHavingInEmail("thoughtworks");
        you.updateCustomerEmail("Dharmendra", newEmail());
    }

    @Test
    public void repeatableReadBlockedByOthersInserts_Inefficient() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
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
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        ArrayList<Customer> customersList = i.getCustomersHavingInName("Ashok");
        you.createCustomer("Ashok Mitra", "amitra@yahoo.com");
        you.commit();
        ArrayList<Customer> newCustomersList = i.getCustomersHavingInName("Ashok");
        assert customersList.size() != newCustomersList.size();
    }

    @Test
    public void noPhantomRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_SERIALIZABLE);
        ArrayList<Customer> customersList = i.getCustomersHavingInName("Ashok");
        try {
            you.createCustomer("Ashok Mitra", "amitra@yahoo.com");
            assert false;
        } catch (BlockedException e) {
            ArrayList<Customer> newCustomersList = i.getCustomersHavingInName("Ashok");
            assert customersList.size() == newCustomersList.size();
        }
    }

    private String newEmail() {
        return String.format("akumar%d@bollywood.com", Calendar.getInstance().getTimeInMillis());
    }
}
