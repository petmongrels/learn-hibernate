package hibernate;

import org.hibernate.SessionFactory;

public interface ISessionFactoryWrapper {
    SessionFactory getSessionFactory();
}
