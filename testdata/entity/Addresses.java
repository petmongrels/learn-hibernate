package entity;

import java.util.ArrayList;

public class Addresses extends ArrayList<Address> {
    public Addresses(Customers customers) {
        add(new Address("Diamond District", "Airport Road", customers.ashokKumar()));
        add(new Address("Habitat", "Brigade Road", customers.ashokKumar()));
    }
}
