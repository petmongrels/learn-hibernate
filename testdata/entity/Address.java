package entity;

public class Address {
    private String line1;
    private String line2;
    private Customer customer;

    public Address(String line1, String line2, Customer customer) {
        this.line1 = line1;
        this.line2 = line2;
        this.customer = customer;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public Customer getCustomer() {
        return customer;
    }
}
