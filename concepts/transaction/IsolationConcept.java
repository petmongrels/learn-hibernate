package transaction;

import database.DatabaseUser;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
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
        } catch (SQLException e) {
            if (!e.getMessage().equals("The query has timed out.")) throw e;
        }
        assert email.equals(i.getCustomerEmail(AshokKumar));
    }

    private String newEmail() {
        return String.format("akumar%d@bollywood.com", Calendar.getInstance().getTimeInMillis());
    }
}
