package hibernate.performance;

import configuration.AppConfigurationImpl;
import database.Databases;
import domain.Address;
import domain.Customer;
import hibernate.HibernateConceptBase;
import hibernate.ISessionFactoryWrapper;
import hibernate.SessionFactoryWrapperFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SecondLevelCacheConcept extends HibernateConceptBase {
    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return SessionFactoryWrapperFactory.create(new AppConfigurationImpl().activeDatabase(), Databases.Main);
    }

    @BeforeMethod
    public void setUp() {
        super.setUp();
        sessionFactory.getCache().evictEntity(Address.class.getName(), 1);
    }

    private void loadAddress() {
        Address address = (Address) session.load(Address.class, 1);
        address.getLine1();
    }

    private long addressLoadCount() {
        return loadCount(Address.class);
    }

    @Test
    public void loads_From_Cache() {
        loadAddress();
        reopenSession();
        loadAddress();

        assert 0 == addressLoadCount();
    }

    @Test
    public void loads_From_Cache_Only_After_Session_Is_Closed_Once() {
        loadAddress();
        clearSession();
        loadAddress();

        assert 1 == addressLoadCount();
    }

    @Test
    public void loading_Via_Collection_Doesnt_Hit_The_Cache() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        loadAddresses(customer);
        reopenSession();

        customer = (Customer) session.load(Customer.class, 1);
        loadAddresses(customer);
        assert 0 != addressLoadCount();
    }

    private void loadAddresses(Customer customer) {
        Address[] addresses = customer.getAddresses();
        for (Address address : addresses) {
            address.getLine1();
        }
    }
}
