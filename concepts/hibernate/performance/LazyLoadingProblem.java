package hibernate.performance;

import domain.BankTransaction;
import domain.City;
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
    public void n_Plus_One_Query() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        customer.getAccounts().size();
        statistics().clear();
        customer.totalTransactedAmount();
        assert loadCount(BankTransaction.class) == 6;
        assert statementCount() == 2;
    }

    @Test
    public void n_Plus_One_Query_For_Many_To_One() {
        List customers = session.createCriteria(Customer.class).list();
        statistics().clear();
        for(Object item : customers) {
            ((Customer) item).getCity().getName();
        }
        assert loadCount(City.class) == 3;
        assert statementCount() == 3;
    }
}
