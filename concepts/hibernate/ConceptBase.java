package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public class ConceptBase {
    protected SessionFactory sessionFactory;
    protected Session session;

    @BeforeTest
    public void testFixtureSetup() {
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    @AfterTest
    public void testFixtureTearDown() {
        if (sessionFactory != null) sessionFactory.close();
    }

    @BeforeMethod
    public void setUp() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterMethod
    public void tearDown() {
        session.getTransaction().commit();
        session.close();
    }
}
