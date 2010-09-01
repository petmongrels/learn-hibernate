package hibernate.performance;

import configuration.AppConfigurationImpl;
import database.Databases;
import domain.*;
import entity.Transaction;
import hibernate.HibernateConceptBase;
import hibernate.ISessionFactoryWrapper;
import hibernate.SessionFactoryWrapperFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.testng.annotations.Test;

import java.util.List;

public class LazyLoadingConcept extends HibernateConceptBase {
    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return SessionFactoryWrapperFactory.create(new AppConfigurationImpl().activeDatabase(), Databases.Main);
    }

    @Test
    public void load_Object_Doesnt_Load_The_Entity() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        assert loadCount() == 0;
        assert !customer.getClass().equals(Customer.class);
        System.out.println(customer.getClass());

        customer = (Customer) session.load(Customer.class, 1000);
        assert customer != null;
    }

    @Test
    public void cannot_Use_Copy_Constructor_With_Lazy_Objects() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        try {
            customer.getId();
            assert loadCount() == 1;
            new Customer(customer);
            assert false;
        } catch (NullPointerException ignored) {
        }
    }

    @Test
    public void get_Doesnt_Load_Proxy() {
        Customer customer = (Customer) session.get(Customer.class, 1);
        assert customer.getClass().equals(Customer.class);

        customer = (Customer) session.get(Customer.class, 1000);
        assert customer == null;
    }

    @Test
    public void children_List_Is_Lazy() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        assert 0 == loadCount(Account.class);
        customer.numberOfAccounts();
        assert 2 == loadCount(Account.class);
    }

    @Test
    public void many_To_One_Entity_Is_Lazy() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        City city = customer.getCity();
        assert 0 == loadCount(City.class);
        assert !city.getClass().equals(City.class);
        System.out.println(city.getClass());
        city.getName();
        assert 1 == loadCount(City.class);
    }

    @Test
    public void no_N_Plus_One_Query() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        customer.getAccounts().size();
        statistics().clear();
        customer.totalTransactedAmount();
        assert loadCount(BankTransaction.class) == 6;
        assert statementCount() == 1;
    }

    @Test
    public void no_N_Plus_One_Query_For_Many_To_One() {
        List customers = session.createCriteria(Customer.class).list();
        statistics().clear();
        for (Object item : customers) {
            ((Customer) item).getCity().getName();
        }
        assert loadCount(City.class) == 3;
        assert statementCount() == 2;        
    }

    @Test
    public void load_Subclass_Doesnt_Return_Subclass_When_Requested_Through_Baseclass() {
        final int id = getCommercialCustomerId();
        try {
            @SuppressWarnings({"UnusedDeclaration"}) final CommercialCustomer commercialCustomer = (CommercialCustomer) session.load(Customer.class, id);
            assert false;
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }
    }

    private int getCommercialCustomerId() {
        final Criteria criteria = session.createCriteria(Customer.class).add(Restrictions.eq("name", "Sholay"));
        Customer customer = (Customer) criteria.uniqueResult();
        final int id = customer.getId();
        session.clear();
        return id;
    }
}
