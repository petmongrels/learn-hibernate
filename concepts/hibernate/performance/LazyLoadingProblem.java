package hibernate.performance;

import domain.Customer;
import hibernate.AltSessionFactoryWrapper;
import hibernate.HibernateConceptBase;
import hibernate.ISessionFactoryWrapper;
import org.testng.annotations.Test;

import java.util.List;

public class LazyLoadingProblem extends HibernateConceptBase {
    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return new AltSessionFactoryWrapper();
    }

    @Test
    public void nPlusOneQuery() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        customer.totalTransactedAmount();
    }

    @Test
    public void nPlusOneQueryForManyToOne() {
        List customers = session.createCriteria(Customer.class).list();
        for(Object item : customers) {
            ((Customer) item).getCity().getName();
        }
    }    
}
