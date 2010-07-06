package repository;

import database.RecordNotModifiedException;
import database.DatabaseConnection;
import domain.Account;

import java.math.BigDecimal;

public class AccountRepository {
    private DatabaseConnection connection;

    public AccountRepository(DatabaseConnection connection) {
        this.connection = connection;
    }

    public Account getAccount(String accountNumber) throws Exception {
        Object[] accountDetails = connection.queryValues("select Id, Balance, Version from Accounts where AccountNumber = ?", accountNumber);
        return new Account(null, Integer.parseInt(accountDetails[0].toString()), (BigDecimal) accountDetails[1], accountNumber, Integer.parseInt(accountDetails[2].toString()));
    }

    public void updateBalanceWithLock(Account account) throws Exception {
        int accountsUpdated = connection.execute("update Accounts set Balance = ? where AccountNumber = ? and Version = ?", account.getBalance(), account.getNumber(), account.getVersion());
        if (accountsUpdated == 0) throw new RecordNotModifiedException();
    }
}
