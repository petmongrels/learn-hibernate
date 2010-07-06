package transaction;

import database.DatabaseUser;
import org.testng.annotations.AfterMethod;

import java.util.Calendar;

public abstract class IsolationConceptBase {
    protected DatabaseUser you;
    protected DatabaseUser i;

    protected abstract String databaseName();

    @AfterMethod
    public void tearDown() throws Exception {
        try {
            if (you != null) {
                you.rollback();
                you.closeConnection();
            }
            if (i != null) {
                i.rollback();
                i.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String newEmail() {
        return String.format("akumar%d@bollywood.com", Calendar.getInstance().getTimeInMillis());
    }
}
