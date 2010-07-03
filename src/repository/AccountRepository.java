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
        Object[] accountDetails = connection.queryValues("select Id, Balance, Version from Accounts where Number = ?", accountNumber);
        return new Account(null, (Integer) accountDetails[0], (BigDecimal) accountDetails[1], accountNumber, (Integer) accountDetails[2]);
    }

    public void updateBalanceWithLock(Account account) throws Exception {
        int accountsUpdated = connection.execute("update Accounts set Balance = ? where Number = ? and Version = ?", account.getBalance(), account.getNumber(), account.getVersion());
        if (accountsUpdated == 0) throw new RecordNotModifiedException();
    }
}
