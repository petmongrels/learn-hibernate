package domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int id;
    private String name;
    private String email;
    private int version;
    private List<Account> accounts;
    private City city;

    protected Customer() {
    }

    public Customer(int id, String name, String email, int version) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.version = version;
    }

    public Customer(Customer customer) {
        this(customer.id, customer.name, customer.email, customer.version);
        city = customer.city.copy();
        version = customer.version;
    }

    public int getVersion() {
        return version;
    }

    public String getEmail() {
        return email;
    }

    public int numberOfAccounts() {
        return accounts.size();
    }

    public BigDecimal totalBalance() {
        BigDecimal totalBalance = new BigDecimal(0);
        for (Account account : accounts) {
            totalBalance = totalBalance.add(account.getBalance());
        }
        return totalBalance;
    }

    public void totalTransactedAmount() {
        BigDecimal transactedAmount = new BigDecimal(0);
        for (Account account : accounts) {
            transactedAmount.add(account.totalTransactedAmount());
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public City getCity() {
        return city;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Customer copy() {
        Customer customer = new Customer(this);
        customer.accounts = new ArrayList<Account>();
        for(Account account : accounts) {
            customer.accounts.add(account.copy());
        }
        return customer;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
