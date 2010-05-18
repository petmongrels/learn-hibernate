package hibernate.performance;

import domain.City;
import domain.CommercialCustomer;
import domain.Customer;
import hibernate.HibernateConceptBase;
import hibernate.ISessionFactoryWrapper;
import hibernate.SessionFactoryWrapper;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.testng.annotations.Test;

import java.util.List;

public class LazyLoadingConcept extends HibernateConceptBase {
    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return new SessionFactoryWrapper();
    }

    @Test
    public void cannotUseCopyConstructorWithLazyObjects() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        try {
            new Customer(customer);
            assert false;
        } catch (NullPointerException ignored) {
        }
    }

    @Test
    public void loadObjectDoesntLoadTheEntity() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        assert !customer.getClass().equals(Customer.class);
        System.out.println(customer.getClass());

        customer = (Customer) session.load(Customer.class, 1000);
        assert customer != null;
    }

    @Test
    public void getDoesntLoadProxy() {
        Customer customer = (Customer) session.get(Customer.class, 1);
        assert customer.getClass().equals(Customer.class);

        customer = (Customer) session.get(Customer.class, 1000);
        assert customer == null;
    }

    @Test
    public void childrenListIsLazy() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        System.out.println("Customer's emailId: " + customer.getEmail());
        System.out.println("Number of accounts: " + customer.numberOfAccounts());
        customer.totalBalance();
    }

    @Test
    public void manyToOne() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        System.out.println("session.load(Customer.class, 1)");
        City city = customer.getCity();
        System.out.println("customer.getCity()");
        System.out.println(city.getClass());
        System.out.println("City: " + city.getName());
    }

    @Test
    public void nPlusOneQueryForManyToOne() {
        List customers = session.createCriteria(Customer.class).list();
        for (Object item : customers) {
            ((Customer) item).getCity().getName();
        }
    }

    @Test
    public void loadSubClassDoesntReturnSubclassWhenRequestedThroughBaseClass() {
        final Criteria criteria = session.createCriteria(Customer.class).add(Restrictions.eq("name", "Sholay"));
        Customer customer = (Customer) criteria.uniqueResult();
        final int id = customer.getId();
        session.clear();
        try {
            @SuppressWarnings({"UnusedDeclaration"}) final CommercialCustomer commercialCustomer = (CommercialCustomer) session.load(Customer.class, id);
            assert false;
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }
    }
}
