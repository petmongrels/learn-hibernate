package domain;

public class City extends Entity {
    private String name;

    protected City() {
    }

    public City(String name) {
        this(0, name);
    }

    public City(int id, String name) {
        super(id);
        this.name = name;
    }

    public City(City city) {
        this(city.getId(), city.name);
    }

    public String getName() {
        return name;
    }

    public City copy() {
        return new City(this);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        if (!super.equals(o)) return false;

        City city = (City) o;

        return !(name != null ? !name.equals(city.name) : city.name != null);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
