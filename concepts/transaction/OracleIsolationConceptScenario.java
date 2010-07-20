package transaction;

import configuration.AppConfigurationImpl;
import configuration.OracleSettings;
import data.Customers;
import database.BlockedException;
import database.DatabaseUser;
import org.testng.annotations.Test;

import java.sql.Connection;

public class OracleIsolationConceptScenario  extends IsolationConceptBase {
    @Test
    public void pessimisticLock() throws Exception {
        i = new DatabaseUser(new OracleSettings(new AppConfigurationImpl()), Connection.TRANSACTION_SERIALIZABLE);
        you = new DatabaseUser(new OracleSettings(new AppConfigurationImpl()), Connection.TRANSACTION_SERIALIZABLE);
        i.getCustomer(Customers.AshokKumar);
        you.getCustomer(Customers.AshokKumar);
        i.updateCustomerEmail(Customers.AshokKumar, newEmail());
        try {
            you.updateCustomerEmail(Customers.AshokKumar, newEmail());
            assert false;
        } catch (BlockedException ignored) {
        }
    }
}
