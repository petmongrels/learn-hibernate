package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class AltSessionFactoryWrapper implements ISessionFactoryWrapper {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = HibernateConfigurationFactory.createBasicConfiguration();

            configuration.addFile(mappingFile("Customer2"));
            configuration.addFile(mappingFile("Account2"));
            configuration.addFile(mappingFile("BankTransaction2"));
            configuration.addFile(mappingFile("City2"));
            return configuration.buildSessionFactory();
        }
        catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static String mappingFile(String mapping) {
        return String.format("src/domain/%s.hbm.xml", mapping);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}