package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.testng.annotations.*;

public class HibernateConceptBase {
    protected SessionFactory sessionFactory;
    protected Session session;

    @BeforeClass
    public void testFixtureSetup() {
        sessionFactory = sessionFactoryWrapper().getSessionFactory();
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
    }

    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        throw new RuntimeException("Cannot make it abstract, because TestNG doesn't work with it.");
    }

    @BeforeMethod
    public void setUp() {
        openSession();
    }

    protected void reopenSession() {
        session.close();
        clearStatistics();
        openSession();
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
        final Transaction transaction = session.getTransaction();
        if (transaction.isActive()) transaction.rollback();
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
