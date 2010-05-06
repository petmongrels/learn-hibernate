package database;

import configuration.AppConfigurationImpl;
import database.sqlserver.SqlServerConnection;
import domain.Customer;
import repository.CustomerRepository;

import java.util.ArrayList;

public class DatabaseUser {
    private final SqlServerConnection connection;
    private final CustomerRepository customerRepository;

    public DatabaseUser(int isolationLevel) throws Exception {
        connection = new SqlServerConnection(new AppConfigurationImpl(), isolationLevel);
        customerRepository = new CustomerRepository(connection);
        connection.beginTransaction();
    }

    public void updateCustomerEmail(String name, String email) throws Exception {
        connection.execute("update Customers set Email = ? where Name = ?", email, name);
    }

    public String getCustomerEmail(String name) throws Exception {
        return customerRepository.getCustomerEmail(name);
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

    public void createCustomer(String name, String email) throws Exception {
        customerRepository.createCustomer(name, email);
    }
}
