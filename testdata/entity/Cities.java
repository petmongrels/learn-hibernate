package entity;

import java.util.ArrayList;

public class Cities extends ArrayList<City> {
    private String bangaloreName = "Bangalore";
    private String mumbaiName = "Mumbai";
    private String calcuttaName = "Calcutta";

    public Cities() {
        add(bangalore());
        add(mumbai());
        add(calcutta());
    }

    public City bangalore() {
        return new City(bangaloreName);
    }

    public City mumbai() {
        return new City(mumbaiName);
    }

    public City calcutta() {
        return new City(calcuttaName);        
    }
}
