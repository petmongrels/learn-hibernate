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

public class VersioningConcept extends HibernateConceptBase {
    private Session otherSession;
    private CustomerService i;
    private CustomerService you;

    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return SessionFactoryWrapperFactory.create(new AppConfigurationImpl().activeDatabase(), Databases.Main);
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        otherSession = sessionFactory.openSession();
        otherSession.beginTransaction();
        i = new CustomerService(session);
        you = new CustomerService(otherSession);
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
        otherSession.close();
    }

    @Test
    public void usingVersionColumn() {
        final Customer customer = i.setCustomerEmail(1, "ask@thoughtworks.com");
        final Customer yourCustomer = you.setCustomerEmail(1, "ak@thoughtworks.com");
        i.updateCustomer(customer);
        try {
            you.updateCustomer(yourCustomer);
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

        Customer yourCustomer = you.setCustomerEmail(1, "ak@thoughtworks.com");
        you.updateCustomer(yourCustomer);
        you.commit();

        session = sessionFactory.openSession();
        session.beginTransaction();
        customer = i.setCustomerEmail(1, "ask@thoughtworks.com");
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

        Customer yourCustomer = you.setCustomerEmail(1, "ak@thoughtworks.com");
        you.updateCustomer(yourCustomer);
        you.commit();        

        session = sessionFactory.openSession();
        session.beginTransaction();
        i = new CustomerService(session);
        customer = i.setCustomerEmail(1, "ask@thoughtworks.com");
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
