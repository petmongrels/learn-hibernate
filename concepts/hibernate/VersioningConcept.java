package hibernate;

import domain.Customer;
import org.hibernate.StaleObjectStateException;
import org.hibernate.classic.Session;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class VersioningConcept extends HibernateConceptBase {
    private Session otherSession;

    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return new SessionFactoryWrapper();
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
    public void usingVersionColumn() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        customer.setEmail("ask@thoughtworks.com");

        Customer customerInOtherSession = (Customer) otherSession.load(Customer.class, 1);
        customerInOtherSession.setEmail("ak@thoughtworks.com");

        otherSession.update(customerInOtherSession);
        otherSession.flush();
        otherSession.getTransaction().commit();
        try {
            session.update(customer);
            session.flush();
            assert false;
        } catch (StaleObjectStateException ignored) {
        }
    }

    @Test
    public void disconnectAndSaveWithOldVersion() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        int disconnectedCustomersVersion = customer.getVersion();
        session.getTransaction().commit();
        session.close();

        Customer customerOfOtherUser = (Customer) otherSession.load(Customer.class, 1);
        customerOfOtherUser.setEmail("ak@thoughtworks.com");
        otherSession.update(customerOfOtherUser);
        otherSession.flush();
        otherSession.getTransaction().commit();

        session = sessionFactory.openSession();
        session.beginTransaction();
        customer = (Customer) session.load(Customer.class, 1);
        customer.setEmail("ask@thoughtworks.com");
        customer.setVersion(disconnectedCustomersVersion);
        session.saveOrUpdate(customer);
        session.flush();
    }
}
