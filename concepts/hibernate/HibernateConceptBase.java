package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public class HibernateConceptBase {
    protected SessionFactory sessionFactory;
    protected Session session;

    @BeforeTest
    public void testFixtureSetup() {
        sessionFactory = sessionFactoryWrapper().getSessionFactory();
        sessionFactory.getStatistics().setStatisticsEnabled(true);
    }

    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        throw new RuntimeException("Cannot make it abstract, because TestNG doesn't work with it.");
    }

    @AfterTest
    public void testFixtureTearDown() {
        if (sessionFactory != null) sessionFactory.close();
    }

    @BeforeMethod
    public void setUp() {
        openSession();
    }

    protected void reopenSession() {
        session.close();
        openSession();
        clearStatistics();
    }

    protected void clearStatistics() {
        sessionFactory.getStatistics().clear();
    }

    protected void clearSession() {
        session.clear();
        clearStatistics();
    }

    protected void openSession() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterMethod
    public void tearDown() {
        session.getTransaction().rollback();
        session.close();
    }

    protected long updateCount(Class aClass) {
        return sessionFactory.getStatistics().getEntityStatistics(aClass.getName()).getUpdateCount();
    }

    protected long insertCount(Class aClass) {
        return sessionFactory.getStatistics().getEntityStatistics(aClass.getName()).getInsertCount();
    }

    protected long loadCount() {
        return sessionFactory.getStatistics().getEntityLoadCount();
    }

    protected long loadCount(Class aClass) {
        return sessionFactory.getStatistics().getEntityStatistics(aClass.getName()).getLoadCount();
    }
}
