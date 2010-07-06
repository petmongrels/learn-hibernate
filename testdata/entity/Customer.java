package entity;

public class Customer {
    private String name;
    private String email;
    private City city;
    private boolean isCommercial;

    public Customer(String name, String email, City city, boolean isCommercial) {
        this.name = name;
        this.email = email;
        this.city = city;
        this.isCommercial = isCommercial;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public City getCity() {
        return city;
    }

    public boolean isCommercial() {
        return isCommercial;
    }
}
