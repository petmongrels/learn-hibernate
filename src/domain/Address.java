package domain;

public class Address extends Entity {
    private String line1;
    private String line2;
    private Customer customer;

    protected Address() {
    }

    public Address(int id, String line1, String line2, Customer customer) {
        super(id);
        this.line1 = line1;
        this.line2 = line2;
        this.customer = customer;
    }

    public Address(Address address) {
        this(address.getId(), address.line1, address.line2, address.customer);
    }

    public Address copy() {
        return new Address(this);
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public void copyFrom(Entity source) {
        Address sourceAddress = (Address) source;
        line1 = sourceAddress.line1;
        line2 = sourceAddress.line2;
    }
}
