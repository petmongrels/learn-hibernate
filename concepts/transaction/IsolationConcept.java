package transaction;

import database.DatabaseUser;
import database.BlockedException;
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
    private final String AshokKumar = "Ashok Kumar";

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
        you.updateCustomerEmail(AshokKumar, newEmail);
        String changedEmail = i.getCustomerEmail("Ashok Kumar");
        assert changedEmail.equals(newEmail);
    }

//    @Test
    public void readCommited_WhenSnapshotModeIsOff() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
        final String newEmail = newEmail();
        you.updateCustomerEmail(AshokKumar, newEmail);
        try {
            i.getCustomerEmail(AshokKumar);
            assert false;
        } catch (BlockedException ignored) {
        }
    }

    @Test
    public void readCommited() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
        final String newEmail = newEmail();
        you.updateCustomerEmail(AshokKumar, newEmail);
        String changedEmail = i.getCustomerEmail(AshokKumar);
        assert !changedEmail.equals(newEmail);

        assert newEmail.equals(you.getCustomerEmail(AshokKumar));
    }

    @Test
    public void nonRepeatableRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED);
        final String email = i.getCustomerEmail(AshokKumar);
        you.updateCustomerEmail(AshokKumar, newEmail());
        you.commit();
        assert !email.equals(i.getCustomerEmail(AshokKumar));
    }

    @Test
    public void repeatableRead() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        final String email = i.getCustomerEmail(AshokKumar);
        try {
            you.updateCustomerEmail(AshokKumar, newEmail());
            assert false;
        } catch (BlockedException e) {
            assert email.equals(i.getCustomerEmail(AshokKumar));
        }
    }

    @Test
    public void lockEscalation() throws Exception {
        i = new DatabaseUser(Connection.TRANSACTION_REPEATABLE_READ);
        i.getCustomersHavingInEmail("bollywood");
        try {
            you.updateCustomerEmail(AshokKumar, newEmail());
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
