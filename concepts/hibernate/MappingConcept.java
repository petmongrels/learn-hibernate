package hibernate;

import configuration.AppConfigurationImpl;
import database.Databases;
import domain.Customer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.testng.annotations.Test;

public class MappingConcept extends HibernateConceptBase {
    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return SessionFactoryWrapperFactory.create(new AppConfigurationImpl().activeDatabase(), Databases.Main);
    }

    @Test
    public void useUniqueToDeleteOneToOneImplementedViaManyToOne() {
        final Criteria criteria = session.createCriteria(Customer.class).add(Restrictions.eq("name", "Jaya Bhaduri"));
        Customer customer = (Customer) criteria.uniqueResult();
        session.delete(customer);
        session.flush();
    }
}
