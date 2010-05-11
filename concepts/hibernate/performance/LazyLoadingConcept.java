package hibernate.performance;

import domain.Customer;
import hibernate.ConceptBase;
import org.testng.annotations.Test;

public class LazyLoadingConcept extends ConceptBase {
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
    public void nPlusOneQueryProblem() {
        //remove the batch-size on the Account-BankTransaction mapping. Also note the batch size is of the parent not of number of children.
        Customer customer = (Customer) session.load(Customer.class, 1);
        customer.totalTransactedAmount();
    }
}
