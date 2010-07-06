package transaction;

import configuration.AppConfigurationImpl;
import configuration.DatabaseSettings;
import database.DatabaseConnection;
import database.DatabaseSettingsFactory;
import domain.Account;
import domain.BankTransaction;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import repository.AccountRepository;

import java.math.BigDecimal;
import java.sql.SQLException;

public class AtomicityConcept {
    private DatabaseConnection connection;
    private BigDecimal balance;
    private Account account;
    private int numberOfTransactions;
    private AccountRepository accountRepository;

    @BeforeClass
    public void setUp() throws Exception {
        final DatabaseSettings databaseSettings = DatabaseSettingsFactory.create(new AppConfigurationImpl());
        connection = new DatabaseConnection(databaseSettings);
        accountRepository = new AccountRepository(connection);
        account = accountRepository.getAccount("SB12345678");
        balance = account.getBalance();
        numberOfTransactions = numberOfTransactions();
    }

    @AfterTest
    public void tearDown() throws Exception {
        connection.close();
    }

    private int numberOfTransactions() throws Exception {
        Object[] objects = connection.queryValues("select count(*) from Transactions where AccountId = ?", account.getId());
        return (Integer)objects[0];
    }

    @Test
    public void noAtomicity() throws Exception {
        try {
            BankTransaction bankTransaction = account.withdraw(new BigDecimal(5000.50));
            connection.execute("update Accounts set Balance = ? where Id = ?", account.getBalance(), account.getId());
            connection.execute("insert into Transactions (Amount, Type, AccountId) values (?, ?, ?)",
                                bankTransaction.getAmount(), bankTransaction.getType(), bankTransaction.getAccount().getId());
            assert false;
        } catch (SQLException e) {
            Account account = accountRepository.getAccount("SB12345678");
            assert !account.getBalance().equals(balance);
            assert numberOfTransactions() == numberOfTransactions;
        }
    }

    @Test
    public void atomic() throws Exception {
         try {
            connection.beginTransaction();
            BankTransaction bankTransaction = account.withdraw(new BigDecimal(5000.50));

            connection.execute("update Accounts set Balance = ? where Id = ?", account.getBalance(), account.getId());
            connection.execute("insert into Transactions (Amount, Type, AccountId) values (?, ?, ?)",
                                bankTransaction.getAmount(), bankTransaction.getType(), bankTransaction.getAccount().getId());
            Assert.fail("BankTransaction insert should have failed");
        } catch (SQLException e) {
            connection.rollback();
            Account account = accountRepository.getAccount("SB12345678");
            assert account.getBalance().equals(balance);
            assert numberOfTransactions() == numberOfTransactions;
        }
    }
}