package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SessionFactoryConcepts {
    private SessionFactory sessionFactory;

    @BeforeTest
    public void testFixtureSetup() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    @AfterTest
    public void testFixtureTearDown() {
        if (sessionFactory != null) sessionFactory.close();
    }

    @Test
    public void alwaysUseSameSessionFactoryAsItIsExpensive() {
        assert sessionFactory != null;
        SessionFactory sessionFactoryAgain = HibernateSessionFactory.getSessionFactory();
        assert sessionFactory == sessionFactoryAgain;
    }

    @Test
    public void useGetCurrentSession() {
        Session session = sessionFactory.getCurrentSession();
        assert session != null;
        assert session == sessionFactory.getCurrentSession();
    }
}
