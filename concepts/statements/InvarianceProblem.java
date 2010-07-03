package statements;

import configuration.AppConfigurationImpl;
import database.Databases;
import domain.City;
import domain.Customer;
import hibernate.HibernateConceptBase;
import hibernate.ISessionFactoryWrapper;
import hibernate.SessionFactoryWrapperFactory;
import org.hibernate.classic.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class InvarianceProblem extends HibernateConceptBase {
    private Session otherSession;

    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return SessionFactoryWrapperFactory.create(new AppConfigurationImpl().activeDatabase(), Databases.Main);
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        otherSession = sessionFactory.openSession();
        otherSession.beginTransaction();
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
        otherSession.getTransaction().rollback();
        otherSession.close();
    }

    @Test
    public void dontCheckUniquenessInCodeBecauseOfRaceConditions() {
        List list = session.createCriteria(City.class).list();

        assertCityAlreadyNotPresent(new City("Chennai"), session);

        otherSession.save(new City("Chennai"));
        otherSession.flush();
        session.save(new City("Chennai"));
        session.flush();

        assert list.size() + 1 == otherSession.createCriteria(City.class).list().size();
        assert list.size() + 1 == session.createCriteria(City.class).list().size();
    }

    @Test
    public void letDatabaseVerifyUniqueness() {
        Customer customerOne = (Customer) session.load(Customer.class, 1);
        Customer customerTwo = (Customer) session.load(Customer.class, 2);
        try {
            customerTwo.setEmail(customerOne.getEmail());
            session.flush();
            assert false;
        } catch (ConstraintViolationException ignored) {
        }
    }

    private void assertCityAlreadyNotPresent(City newCity, Session session) {
        List cities = session.createCriteria(City.class).list();
        if (cities.contains(newCity))
            assert false;
    }
}
