package service;

import domain.providers.AccountNumberProvider;
import domain.Customer;
import org.hibernate.Session;

import java.math.BigDecimal;

public class CustomerService {
    private Session session;

    public CustomerService(Session session) {
        this.session = session;
    }

    public Customer setCustomerEmail(int id, String email) {
        Customer customer = (Customer) session.load(Customer.class, id);
        customer.setEmail(email);
        return customer;
    }

    public void updateAndCommit(Customer customer) {
        session.update(customer);
        commit();
    }

    public Customer getCustomer(int id) {
        return (Customer) session.load(Customer.class, id);
    }

    public void withdrawFromFirstAccount(Customer customer, BigDecimal amount) {
        customer.getAccounts().get(0).withdraw(amount);
    }

    public void commit() {
        session.flush();
        session.getTransaction().commit();
    }

    public void close() {
        session.close();
    }

    public Customer addAccount(Customer customer, BigDecimal startingAmount, AccountNumberProvider accountNumberProvider) {
        customer.newAccount(startingAmount, accountNumberProvider);
        return customer;
    }
}
