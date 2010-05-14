package hibernate;

import domain.Customer;
import org.hibernate.exception.ConstraintViolationException;
import org.testng.annotations.Test;

public class SaveProblems extends HibernateConceptBase {
    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return new AltSessionFactoryWrapper();
    }

    @Test
    public void saveOrUpdateFailsIfInverseIsFalse() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        Customer clonedCustomer = customer.copy();
        clonedCustomer.setEmail("aksx@thoughtworks.com");
        session.clear();
        session.saveOrUpdate(clonedCustomer);
        try {
            session.flush();
            assert false;
        } catch (ConstraintViolationException e) {
            System.out.println(e.getMessage());
        }
    }
}
