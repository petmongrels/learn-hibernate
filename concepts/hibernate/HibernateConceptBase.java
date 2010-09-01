package hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.stat.Statistics;
import org.testng.annotations.*;

public class HibernateConceptBase {
    protected SessionFactory sessionFactory;
    protected Session session;

    @BeforeClass
    public void testFixtureSetup() {
        sessionFactory = sessionFactoryWrapper().getSessionFactory();
        statistics().setStatisticsEnabled(true);
        statistics().clear();
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
        statistics().clear();
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
        return statistics().getEntityStatistics(aClass.getName()).getUpdateCount();
    }

    protected long insertCount(Class aClass) {
        return statistics().getEntityStatistics(aClass.getName()).getInsertCount();
    }

    protected Statistics statistics() {
        return sessionFactory.getStatistics();
    }

    protected long loadCount() {
        return statistics().getEntityLoadCount();
    }

    protected long loadCount(Class aClass) {
        return statistics().getEntityStatistics(aClass.getName()).getLoadCount();
    }

    protected long statementCount() {
        return statistics().getPrepareStatementCount();
    }
}
