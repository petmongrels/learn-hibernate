package database;

import configuration.AppConfigurationImpl;
import database.sqlserver.SqlServerConnection;
import domain.Account;
import domain.Customer;
import repository.AccountRepository;
import repository.CustomerRepository;

import java.util.ArrayList;

public class DatabaseUser {
    private final SqlServerConnection connection;
    private final CustomerRepository customerRepository;
    private AccountRepository accountRepository;

    public DatabaseUser(int isolationLevel, String database) throws Exception {
        connection = new SqlServerConnection(new AppConfigurationImpl(), isolationLevel, database);
        customerRepository = new CustomerRepository(connection);
        accountRepository = new AccountRepository(connection);
        connection.beginTransaction();
    }

    public int updateCustomerEmail(String name, String email) throws Exception {
        return connection.execute("update Customers set Email = ? where Name = ?", email, name);
    }

    public int updateCustomerEmail(String name, String email, int version) throws Exception {
        return connection.execute("update Customers set Email = ?, Version = ? where Name = ? and Version = ?", email, version + 1, name, version);
    }

    public Customer getCustomer(String name) throws Exception {
        return customerRepository.getCustomer(name);
    }

    public void rollback() throws Exception {
        connection.rollback();
    }

    public void closeConnection() throws Exception {
        connection.close();
    }

    public void commit() throws Exception {
        connection.commit();
    }

    public ArrayList<Customer> getCustomersHavingInName(String nameToken) throws Exception {
        return customerRepository.getCustomersHavingInName(nameToken);
    }

    public ArrayList<Customer> getCustomersHavingInEmail(String emailToken) throws Exception {
        return customerRepository.getCustomersHavingInEmail(emailToken);
    }

    public void createCustomer(String name, String email) throws Exception {
        customerRepository.createCustomer(name, email);
    }

    public Account getAccount(String number) throws Exception {
        return accountRepository.getAccount(number);
    }

    public void updateAccountBalance(Account account) throws Exception {
        accountRepository.updateBalanceWithLock(account);
    }

    public void updateAccountBalanceWithLock(Account account) throws Exception {
        accountRepository.updateBalanceWithLock(account);
    }
}
