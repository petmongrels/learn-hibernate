package repository;

import database.DatabaseConnection;
import domain.Customer;

import java.util.ArrayList;

public class CustomerRepository {
    private final DatabaseConnection connection;

    public CustomerRepository(DatabaseConnection connection) {
        this.connection = connection;
    }

    public Customer getCustomer(String name) throws Exception {
        Object[] row = connection.queryValues("select Id, Name, Email, Version from Customers where Name = ?", name);
        return createCustomer(row);
    }

    private Customer createCustomer(Object[] row) {
        return new Customer((Integer)row[0], (String)row[1], (String)row[2], (Integer)row[3]);
    }

    public ArrayList<Customer> getCustomersHavingInName(String token) throws Exception {
        return getCustomers(token, "select Id, Name, Email, Version from Customers where Name like ?");
    }

    private ArrayList<Customer> getCustomers(String token, String sqlQuery) throws Exception {
        final ArrayList<Object[]> rows = connection.queryRows(sqlQuery, "%" + token + "%");
        final ArrayList<Customer> customers = new ArrayList<Customer>();
        for(Object[] row : rows) {
            customers.add(createCustomer(row));
        }
        return customers;
    }

    public ArrayList<Customer> getCustomersHavingInEmail(String token) throws Exception {
        return getCustomers(token, "select Id, Name, Email, Version from Customers where Email like ?");
    }

    public void createCustomer(String name, String email) throws Exception {
        connection.execute("insert into Customers (Name, Email, CityId) values (?, ?, ?)", name, email, 1);
    }
}
