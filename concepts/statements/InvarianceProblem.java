package statements;

import domain.City;
import hibernate.AltSessionFactoryWrapper;
import hibernate.HibernateConceptBase;
import hibernate.ISessionFactoryWrapper;
import org.hibernate.classic.Session;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class InvarianceProblem extends HibernateConceptBase {
    private Session otherSession;

    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return new AltSessionFactoryWrapper();
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
        otherSession.close();
    }

    @Test
    public void dontCheckUniquenessInCodeBecauseOfRaceConditions() {
        City newCity = new City("Chennai");

        isCityAlreadyPresent(newCity, session);
        isCityAlreadyPresent(newCity, otherSession);

        otherSession.save(newCity);
        otherSession.flush();
        session.save(newCity);
        session.flush();
    }

    private void isCityAlreadyPresent(City newCity, Session session) {
        List cities = session.createCriteria(City.class).list();
        if (cities.contains(newCity))
            assert false;
    }
}
