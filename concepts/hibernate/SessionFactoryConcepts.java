package hibernate;

import configuration.AppConfigurationImpl;
import database.Databases;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SessionFactoryConcepts {
    private SessionFactory sessionFactory;

    @BeforeTest
    public void testFixtureSetup() {
        final ISessionFactoryWrapper sessionFactoryWrapper = SessionFactoryWrapperFactory.create(new AppConfigurationImpl().activeDatabase(), Databases.Main);
        sessionFactory = sessionFactoryWrapper.getSessionFactory();
    }

    @AfterTest
    public void testFixtureTearDown() {
        if (sessionFactory != null) sessionFactory.close();
    }

    @Test
    public void alwaysUseSameSessionFactoryAsItIsExpensive() {
        assert sessionFactory != null;
        final ISessionFactoryWrapper sessionFactoryWrapper = SessionFactoryWrapperFactory.create(new AppConfigurationImpl().activeDatabase(), Databases.Main);
        SessionFactory sessionFactoryAgain = sessionFactoryWrapper.getSessionFactory();
        assert sessionFactory == sessionFactoryAgain;
    }

    @Test
    public void useGetCurrentSession() {
        Session session = sessionFactory.getCurrentSession();
        assert session != null;
        try {
            assert session == sessionFactory.getCurrentSession();
        } finally {
            session.close();
        }
    }

    @Test
    public void openSession() {
        Session session = sessionFactory.openSession();
        Session otherSession = sessionFactory.openSession();
        try {
            assert !session.equals(otherSession);
        } finally {
            session.close();
            otherSession.close();
        }
    }
}
