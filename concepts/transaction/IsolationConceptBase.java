package transaction;

import database.DatabaseUser;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.sql.Connection;
import java.util.Calendar;

public abstract class IsolationConceptBase {
    protected DatabaseUser you;
    protected DatabaseUser i;

    @BeforeMethod
    public void setUp() throws Exception {
        you = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, databaseName());
    }

    protected abstract String databaseName();

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

    protected String newEmail() {
        return String.format("akumar%d@bollywood.com", Calendar.getInstance().getTimeInMillis());
    }
}
