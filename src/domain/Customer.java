package domain;

public class Customer {
    private final int id;
    private final String name;
    private final String email;
    private final int version;

    public Customer(int id, String name, String email, int version) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public String getEmail() {
        return email;
    }
}
