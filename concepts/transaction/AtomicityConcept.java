package transaction;

import configuration.AppConfigurationImpl;
import database.sqlserver.SqlServerConnection;
import domain.Account;
import domain.BankTransaction;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import repository.CustomerRepository;

import java.math.BigDecimal;
import java.sql.SQLException;

public class AtomicityConcept {
    private SqlServerConnection connection;
    private CustomerRepository customerRepository;
    private BigDecimal balance;
    private Account account;

    @BeforeTest
    public void SetUp() throws Exception {
        connection = new SqlServerConnection(new AppConfigurationImpl());
        customerRepository = new CustomerRepository(connection);
        account = customerRepository.getAccount("SB12345678");
        balance = account.getBalance();
    }

    @AfterTest
    public void TearDown() throws Exception {
        connection.close();
    }

    @Test
    public void NoAtomicity() throws Exception {
        try {
            BankTransaction bankTransaction = account.withdraw(new BigDecimal(5000.50));

            connection.execute("update Accounts set Balance = ? where Id = ?", account.getBalance(), account.getId());
            connection.execute("insert into Transactions (Amount, Type, AccountId) values (?, ?, ?)",
                                bankTransaction.getAmount(), bankTransaction.getTransactionType().toString(), bankTransaction.getAccount().getId());
            Assert.fail("BankTransaction insert should have failed");
        } catch (SQLException e) {
            Account account = customerRepository.getAccount("SB12345678");
            Assert.assertFalse(account.getBalance().equals(balance));
        }
    }

    @Test
    public void AtomicYay() throws Exception {
         try {
            connection.beginTransaction();
            BankTransaction bankTransaction = account.withdraw(new BigDecimal(5000.50));

            connection.execute("update Accounts set Balance = ? where Id = ?", account.getBalance(), account.getId());
            connection.execute("insert into Transactions (Amount, Type, AccountId) values (?, ?, ?)",
                                bankTransaction.getAmount(), bankTransaction.getTransactionType().toString(), bankTransaction.getAccount().getId());
            Assert.fail("BankTransaction insert should have failed");
        } catch (SQLException e) {
            connection.rollback();
            Account account = customerRepository.getAccount("SB12345678");
            Assert.assertTrue(account.getBalance().equals(balance));
        }
    }
}