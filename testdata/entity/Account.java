package entity;

public class Account {
    private String number;
    private Customer customer;
    private double balance;

    public Account(String number, Customer customer, double balance) {
        this.number = number;
        this.customer = customer;
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getBalance() {
        return balance;
    }
}
