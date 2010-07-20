package hibernate;

import domain.Customer;
import org.testng.annotations.Test;

public class OracleSaveConcepts extends HibernateConceptBase {
    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return new OracleSessionFactoryWrapper();
    }

    @Test
    public void save_Or_Update_Works_Without_Inverse_Relationship_Using_Deferred_Constraints() {
        Customer customer = (Customer) session.load(Customer.class, 1);
        Customer clonedCustomer = customer.copy();
        clonedCustomer.setEmail("aksx@thoughtworks.com");
        session.clear();
        session.saveOrUpdate(clonedCustomer);
        session.flush();
    }
}
