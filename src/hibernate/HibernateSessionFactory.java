package hibernate;

import configuration.AppConfigurationImpl;
import domain.Account;
import domain.BankTransaction;
import domain.Customer;
import org.hibernate.SessionFactory;
import org.hibernate.cache.NoCacheProvider;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.SQLServerDialect;

public class HibernateSessionFactory {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            AppConfigurationImpl appConfiguration = new AppConfigurationImpl();
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", net.sourceforge.jtds.jdbc.Driver.class.getName());
            configuration.setProperty("hibernate.connection.dialect", SQLServerDialect.class.getName());
            configuration.setProperty("hibernate.connection.url", "jdbc:jtds:sqlserver://" + appConfiguration.sqlServer() + ":1433/LearnHibernate;SelectMethod=cursor");
            configuration.setProperty("hibernate.connection.username", appConfiguration.sqlServerUser());
            configuration.setProperty("hibernate.connection.password", appConfiguration.sqlServerPassword());
            configuration.setProperty("hibernate.connection.pool_size", "1");
            configuration.setProperty("hibernate.current_session_context_class", "thread");
            configuration.setProperty("hibernate.cache.provider_class", NoCacheProvider.class.getName());
            configuration.setProperty("hibernate.show_sql", "true");

            configuration.addClass(Customer.class);
            configuration.addClass(Account.class);
            configuration.addClass(BankTransaction.class);
            return configuration.buildSessionFactory();
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
