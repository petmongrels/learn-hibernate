package database;

import configuration.AppConfigurationImpl;
import configuration.DatabaseSettings;
import domain.Account;
import domain.Customer;
import repository.AccountRepository;
import repository.CustomerRepository;

import java.util.ArrayList;

public class DatabaseUser {
    private final DatabaseConnection connection;
    private final CustomerRepository customerRepository;
    private AccountRepository accountRepository;

    public DatabaseUser(String database, int isolationLevel) throws Exception {
        this(DatabaseSettingsFactory.create(new AppConfigurationImpl(), database), isolationLevel);
    }

    public DatabaseUser(DatabaseSettings databaseSettings, int isolationLevel) throws Exception {
        connection = new DatabaseConnection(databaseSettings, isolationLevel);
        customerRepository = new CustomerRepository(connection);
        accountRepository = new AccountRepository(connection);
        connection.beginTransaction();
    }

    public int updateCustomerEmail(String name, String email) throws Exception {
        return connection.execute("update Customers set Email = ? where Name = ?", email, name);
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

    public void beginTransaction() throws Exception {
        connection.beginTransaction();
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
