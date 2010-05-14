package domain;

public class City {
    private int id;
    private String name;

    protected City() {
    }

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(City city) {
        this(city.id, city.name);
    }

    public String getName() {
        return name;
    }

    public City copy() {
        return new City(this);
    }
}
