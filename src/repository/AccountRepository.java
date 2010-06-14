package repository;

import database.RecordNotModifiedException;
import database.sqlserver.SqlServerConnection;
import domain.Account;

import java.math.BigDecimal;

public class AccountRepository {
    private SqlServerConnection connection;

    public AccountRepository(SqlServerConnection connection) {
        this.connection = connection;
    }

    public Account getAccount(String accountNumber) throws Exception {
        Object[] accountDetails = connection.queryValues("select Id, Balance, Version from Accounts where AccountNumber = ?", accountNumber);
        return new Account(null, (Integer) accountDetails[0], (BigDecimal) accountDetails[1], accountNumber, (Integer) accountDetails[2]);
    }

    public void updateBalanceWithLock(Account account) throws Exception {
        int accountsUpdated = connection.execute("update Accounts set Balance = ? where AccountNumber = ? and Version = ?", account.getBalance(), account.getNumber(), account.getVersion());
        if (accountsUpdated == 0) throw new RecordNotModifiedException();
    }
}
