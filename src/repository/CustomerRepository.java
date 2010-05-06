package repository;

import database.sqlserver.SqlServerConnection;
import domain.Account;
import domain.Customer;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CustomerRepository {
    private final SqlServerConnection connection;

    public CustomerRepository(SqlServerConnection connection) {
        this.connection = connection;
    }

    public Account getAccount(String accountNumber) throws Exception {
        Object[] accountDetails = connection.queryValues("select Id, Balance from Accounts where Number = ?", accountNumber);
        return new Account((Integer) accountDetails[0], (BigDecimal) accountDetails[1], accountNumber);
    }

    public String getCustomerEmail(String name) throws Exception {
        Object[] customerValues = connection.queryValues("select Email from Customers where Name = ?", name);
        return (String) customerValues[0];
    }

    public ArrayList<Customer> getCustomersHavingInName(String nameToken) throws Exception {
        final ArrayList<Object[]> rows = connection.queryRows("select Id, Name from Customers where name like ?", "%" + nameToken + "%");
        final ArrayList<Customer> customers = new ArrayList<Customer>();
        for(Object[] row : rows) {
            customers.add(new Customer((Integer)row[0], (String)row[1]));
        }
        return customers;
    }

    public void createCustomer(String name, String email) throws Exception {
        connection.execute("insert into Customers (Name, Email) values (?, ?)", name, email);
    }
}
