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
        sessionFactory = new SessionFactoryWrapper().getSessionFactory();
    }

    @AfterTest
    public void testFixtureTearDown() {
        if (sessionFactory != null) sessionFactory.close();
    }

    @Test
    public void alwaysUseSameSessionFactoryAsItIsExpensive() {
        assert sessionFactory != null;
        SessionFactory sessionFactoryAgain = new SessionFactoryWrapper().getSessionFactory();
        assert sessionFactory == sessionFactoryAgain;
    }

    @Test
    public void useGetCurrentSession() {
        Session session = sessionFactory.getCurrentSession();
        try {
            assert session != null;
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
