package hibernate;

import configuration.AppConfigurationImpl;
import configuration.SqlServerSettings;
import domain.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SqlServerSessionFactoryWrapper implements ISessionFactoryWrapper {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = HibernateConfigurationFactory.createBasicConfiguration(new SqlServerSettings(new AppConfigurationImpl()));
            configuration.addClass(Customer.class);
            configuration.addClass(Account.class);
            configuration.addClass(BankTransaction.class);
            configuration.addClass(City.class);
            configuration.addClass(Address.class);
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
