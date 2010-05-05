package repository;

import database.sqlserver.SqlServerConnection;
import domain.Account;

import java.math.BigDecimal;

public class CustomerRepository {
    private SqlServerConnection connection;

    public CustomerRepository(SqlServerConnection connection) {
        this.connection = connection;
    }

    public Account getAccount(String accountNumber) throws Exception {
        Object[] accountDetails = connection.queryValues("select Id, Balance from Accounts where Number = ?", accountNumber);
        return new Account((Integer) accountDetails[0], (BigDecimal) accountDetails[1], accountNumber);
    }
}
