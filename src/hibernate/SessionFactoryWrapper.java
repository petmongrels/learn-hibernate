package hibernate;

import domain.Account;
import domain.BankTransaction;
import domain.City;
import domain.Customer;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryWrapper implements ISessionFactoryWrapper {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = HibernateConfigurationFactory.createBasicConfiguration();

            configuration.addClass(Customer.class);
            configuration.addClass(Account.class);
            configuration.addClass(BankTransaction.class);
            configuration.addClass(City.class);
            return configuration.buildSessionFactory();
        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
