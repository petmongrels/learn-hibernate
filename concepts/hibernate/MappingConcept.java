package hibernate;

public class MappingConcept extends HibernateConceptBase {
    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return new SessionFactoryWrapper();
    }
}
