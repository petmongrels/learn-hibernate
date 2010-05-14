package hibernate;

import domain.Customer;
import org.testng.annotations.Test;

public class SaveConcepts extends HibernateConceptBase {
    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return new SessionFactoryWrapper();
    }
    
    @Test
    public void saveOrUpdateDisconnectedObject(){
        Customer customer = (Customer) session.load(Customer.class, 1);
        Customer clonedCustomer = customer.copy();
        clonedCustomer.setEmail("aksx@thoughtworks.com");
        session.clear();
        session.saveOrUpdate(clonedCustomer.getAccounts().get(0));
        session.flush();
    }
}
