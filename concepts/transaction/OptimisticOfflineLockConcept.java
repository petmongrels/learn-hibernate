package transaction;

import database.DatabaseUser;
import database.Databases;
import database.RecordNotModifiedException;
import domain.Account;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Connection;

public class OptimisticOfflineLockConcept {
    private DatabaseUser you;
    private DatabaseUser i;

    @BeforeClass
    public void setUp() throws Exception {
        you = new DatabaseUser(Databases.Main, Connection.TRANSACTION_READ_COMMITTED);
        i = new DatabaseUser(Databases.Main, Connection.TRANSACTION_READ_COMMITTED);
    }

    @AfterTest
    public void tearDown() throws Exception {
        you.rollback();
        you.closeConnection();
        i.rollback();
        i.closeConnection();
    }

    @Test
    public void free_Money() throws Exception {
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
    public void update_Lock() throws Exception {
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
