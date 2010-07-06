package hibernate;

import configuration.AppConfigurationImpl;
import database.Databases;
import domain.Customer;
import domain.providers.SavingsAccountNumberProvider;
import org.hibernate.StaleObjectStateException;
import org.hibernate.classic.Session;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.CustomerService;

import java.math.BigDecimal;
import java.util.UUID;

public class VersioningConcept extends HibernateConceptBase {
    private Session yourSession;
    private CustomerService i;
    private CustomerService you;

    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return SessionFactoryWrapperFactory.create(new AppConfigurationImpl().activeDatabase(), Databases.Main);
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        yourSession = sessionFactory.openSession();
        i = new CustomerService(session);
        you = new CustomerService(yourSession);
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
        yourSession.close();
    }

    @Test
    public void usingVersionColumn() {
        final Customer customer = i.setCustomerEmail(1, "ask" + UUID.randomUUID().toString() + "@thoughtworks.com");
        final Customer yourCustomer = you.setCustomerEmail(1, "ak" + UUID.randomUUID().toString() + "@thoughtworks.com");
        
        i.updateAndCommit(customer);
        
        yourSession.beginTransaction();
        try {
            you.updateAndCommit(yourCustomer);
            assert false;
        } catch (StaleObjectStateException ignored) {
        }
    }

    @Test
    public void disconnectAndSaveWithOldVersion() {
        Customer customer = i.getCustomer(1);
        int disconnectedCustomersVersion = customer.getVersion();
        i.commit();
        i.close();

        yourSession.beginTransaction();
        Customer yourCustomer = you.setCustomerEmail(1, "ak" + UUID.randomUUID().toString() + "@thoughtworks.com");
        you.updateAndCommit(yourCustomer);

        session = sessionFactory.openSession();
        session.beginTransaction();
        i = new CustomerService(session);
        customer = i.setCustomerEmail(1, "ask"  + UUID.randomUUID().toString() + "@thoughtworks.com");
        customer.setVersion(disconnectedCustomersVersion);
        session.saveOrUpdate(customer);
        session.flush();
    }

    @Test
    public void verifyVersionYourselfForDisconnectLockingScope(){
        Customer customer = i.getCustomer(1);
        int disconnectedCustomersVersion = customer.getVersion();
        i.commit();
        i.close();

        yourSession.beginTransaction();
        Customer yourCustomer = you.setCustomerEmail(1, "ak" + UUID.randomUUID().toString() + "@thoughtworks.com");
        you.updateAndCommit(yourCustomer);

        session = sessionFactory.openSession();
        session.beginTransaction();
        i = new CustomerService(session);
        customer = i.setCustomerEmail(1, "ask" + UUID.randomUUID().toString() + "@thoughtworks.com");
        try {
            customer.verifyVersion(disconnectedCustomersVersion);
            assert false;
        } catch (StaleObjectStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void noChangeAndFlushDoesntChangeTheVersion(){
        Customer customer = i.getCustomer(1);
        int version = customer.getVersion();
        session.flush();
        assert version == customer.getVersion();
    }

    @Test
    public void noChangeToParentDoesnUpdateVersion(){
        Customer customer = i.getCustomer(1);
        int version = customer.getVersion();
        i.withdrawFromFirstAccount(customer, new BigDecimal(100));
        session.saveOrUpdate(customer);
        session.flush();
        assert version == customer.getVersion();
    }

    @Test
    public void newChildChangesTheParentsVersion() {
        Customer customer = i.getCustomer(1);
        int version = customer.getVersion();
        i.addAccount(customer, new BigDecimal(100), new SavingsAccountNumberProvider());
        session.saveOrUpdate(customer);
        session.flush();
        assert version != customer.getVersion();
    }

    @Test
    public void saveOrUpdateWithoutChangeDoesntChangeTheVersion() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        int version = customer.getVersion();
        session.saveOrUpdate(customer);
        session.flush();
        assert version == customer.getVersion();
    }
}
