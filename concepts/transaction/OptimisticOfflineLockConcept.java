package transaction;

import database.DatabaseUser;
import database.Databases;
import database.RecordNotModifiedException;
import domain.Account;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Connection;

public class OptimisticOfflineLockConcept {
    private DatabaseUser you;
    private DatabaseUser i;

    @BeforeTest
    public void setUp() throws Exception {
        you = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, Databases.Main);
        i = new DatabaseUser(Connection.TRANSACTION_READ_COMMITTED, Databases.Main);
    }

    @AfterTest
    public void tearDown() throws Exception {
        you.rollback();
        you.closeConnection();
        i.rollback();
        i.closeConnection();
    }

    @Test
    public void freeMoney() throws Exception {
        Account myVersionOfAccount = i.getAccount("SB12345678");
        final BigDecimal balance = myVersionOfAccount.getBalance();

        Account yourVersionOfAccount = you.getAccount("SB12345678");
        myVersionOfAccount.withdraw(new BigDecimal(100));
        yourVersionOfAccount.withdraw(new BigDecimal(100));
        i.updateAccountBalance(myVersionOfAccount);
        i.commit();
        you.updateAccountBalance(yourVersionOfAccount);
        you.commit();

        BigDecimal latestBalance = myVersionOfAccount.getBalance();
        assert latestBalance.equals(balance.subtract(new BigDecimal(100)));
    }

    @Test
    public void updateLock() throws Exception {
        Account myVersionOfAccount = i.getAccount("SB12345678");
        Account yourVersionOfAccount = you.getAccount("SB12345678");
        
        myVersionOfAccount.withdraw(new BigDecimal(100));
        yourVersionOfAccount.withdraw(new BigDecimal(100));
        i.updateAccountBalanceWithLock(myVersionOfAccount);
        i.commit();
        try {
            you.updateAccountBalanceWithLock(yourVersionOfAccount);
        } catch (RecordNotModifiedException ignored) {
        }
    }
}
