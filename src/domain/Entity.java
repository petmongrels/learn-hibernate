package domain;

import org.hibernate.StaleObjectStateException;

public abstract class Entity {
    private int id;
    protected int version;

    private static int UnVersioned = -1;

    protected Entity() {
    }

    protected Entity(int id, int version) {
        this.id = id;
        this.version = version;
    }

    protected Entity(int id) {
        this(id, UnVersioned);
    }

    public int getId(){
        return id;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;
        return id == entity.id;
    }

    public int hashCode() {
        return id;
    }

    public void copyFrom(Entity source){}

    public void verifyVersion(int disconnectedCustomersVersion) {
        if (version != disconnectedCustomersVersion) throw new StaleObjectStateException(getClass().getName(), id);
    }

    public int getVersion() {
        return version;
    }
}
