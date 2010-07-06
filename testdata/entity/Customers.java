package entity;

import java.util.ArrayList;

public class Customers extends ArrayList<Customer> {
    private Cities cities;

    public Customers(Cities cities) {
        this.cities = cities;
        add(ashokKumar());
        addCustomer("Amitabh Bachchan", "amitabh@bollywood.com", cities.bangalore());
        addCustomer("Dharmendra", "Dharmendra@bollywood.com", cities.bangalore());
        addCustomer("Hema Malini", "hema@bollywood.com", cities.bangalore());
        addCustomer("Sanjiv Kumar", "skumar@bollywood.com", cities.bangalore());
        addCustomer("Amjad Khan", "amjad@bollywood.com", cities.mumbai());
        addCustomer("A. K. Hangal", "akhangal@bollywood.com", cities.mumbai());
        addCustomer("Leela Mishra", "lmishra@bollywood.com", cities.mumbai());
        addCustomer("Asrani", "asrani@bollywood.com", cities.mumbai());
        addCustomer("Jaya Bhaduri", "jaya@bollywood.com", cities.calcutta());
        addCustomer("Sholay", "sholay@bollywood.com", cities.mumbai(), true);
    }

    private void addCustomer(String name, String email, City city) {
        addCustomer(name, email, city, false);
    }

    private void addCustomer(String name, String email, City city, boolean isCommercial) {
        add(new Customer(name, email, city, isCommercial));
    }

    public Customer ashokKumar() {
        return new Customer("Ashok Kumar", "akumar@thoughtworks.com", cities.bangalore(), false);
    }
}
