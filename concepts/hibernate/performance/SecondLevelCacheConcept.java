package hibernate.performance;

import domain.Address;
import domain.Customer;
import hibernate.HibernateConceptBase;
import hibernate.ISessionFactoryWrapper;
import hibernate.SessionFactoryWrapper;
import org.testng.annotations.Test;

public class SecondLevelCacheConcept extends HibernateConceptBase {
    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return new SessionFactoryWrapper();
    }

    @Test
    public void loadsFromCache() {
        Address address = (Address) session.load(Address.class, 1);
        address.getLine1();
        reopenSession();
        address = (Address) session.load(Address.class, 1);
        address.getLine1();
    }

    @Test
    public void loadsFromCacheOnlyAfterSessionIsClosedOnce() {
        Address address = (Address) session.load(Address.class, 1);
        address.getLine1();
        session.clear();
        address = (Address) session.load(Address.class, 1);
        address.getLine1();
    }

    @Test
    public void loadThroughCollectionAddsToTheCache() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        loadAddresses(customer);
        reopenSession();
        session.load(Address.class, 1);
    }

    @Test
    public void loadingViaCollectionDoesntHitTheCache() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        loadAddresses(customer);
        reopenSession();
        System.out.println("Reopened Session");
        session.load(Address.class, 1);
        System.out.println("Address loaded");
        customer = (Customer) session.load(Customer.class, 1);
        loadAddresses(customer);
        System.out.println("Doesn't hit cache");
    }

    private void loadAddresses(Customer customer) {
        Address[] addresses = customer.getAddresses();
        for (Address address : addresses) {
            address.getLine1();
        }
    }
}
