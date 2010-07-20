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
    private Session you;
    private Session i;

    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return SessionFactoryWrapperFactory.create(new AppConfigurationImpl().activeDatabase(), Databases.Main);
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        you = sessionFactory.openSession();
        you.beginTransaction();
        i = session;
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
        you.getTransaction().rollback();
        you.close();
    }

    @Test
    public void dontCheckUniquenessInCodeBecauseOfRaceConditions() {
        List list = i.createCriteria(City.class).list();

        assertCityAlreadyNotPresent(new City("Chennai"), i);

        you.save(new City("Chennai"));
        you.flush();
        i.save(new City("Chennai"));
        i.flush();

        assert list.size() + 1 == you.createCriteria(City.class).list().size();
        assert list.size() + 1 == you.createCriteria(City.class).list().size();
    }

    @Test
    public void letDatabaseVerifyUniqueness() {
        Customer customerOne = (Customer) i.load(Customer.class, 1);
        Customer customerTwo = (Customer) i.load(Customer.class, 2);
        try {
            customerTwo.setEmail(customerOne.getEmail());
            i.flush();
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
