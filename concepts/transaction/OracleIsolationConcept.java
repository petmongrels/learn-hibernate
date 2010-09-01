package transaction;

import data.Customers;
import database.DatabaseUser;
import database.Databases;
import domain.Customer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class OracleIsolationConcept extends IsolationConceptBase {
    @BeforeMethod
    public void setUp() throws Exception {
        you = new DatabaseUser(Databases.Main, Connection.TRANSACTION_READ_COMMITTED);
    }

    @Test
    public void cannot_Read_Uncommited_Data() throws Exception {
        try {
            i = new DatabaseUser(Databases.Main, Connection.TRANSACTION_READ_UNCOMMITTED);
            assert false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void repeatable_Read() throws Exception {
        i = new DatabaseUser(Databases.Main, Connection.TRANSACTION_SERIALIZABLE);
        Customer customer = i.getCustomer(Customers.AshokKumar);
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
        assert customer.getEmail().equals(i.getCustomer(Customers.AshokKumar).getEmail());
    }

    @Test
    public void phantom_Read() throws Exception {
        final UUID uuid = UUID.randomUUID();
        i = new DatabaseUser(Databases.Main, Connection.TRANSACTION_READ_COMMITTED);
        ArrayList<Customer> customersList = i.getCustomersHavingInName("Ashok");
        you.createCustomer("Ashok Mitra", "amitra" + uuid.toString() + "@yahoo.com");
        you.commit();
        ArrayList<Customer> newCustomersList = i.getCustomersHavingInName("Ashok");
        assert customersList.size() + 1 == newCustomersList.size();
    }

    @Test
    public void repeatable_Read_Not_Blocked_By_Others_Inserts() throws Exception {
        i = new DatabaseUser(Databases.Main, Connection.TRANSACTION_SERIALIZABLE);
        i.getCustomersHavingInName("Ashok");
        you.createCustomer("Kishore Kumar", "kkumar@yahoo.com");
        i.getCustomersHavingInName("Ashok");
    }

    @Test
    public void no_Lock_Escalation_Even_When_Majority_Of_Rows_Read() throws Exception {
        i = new DatabaseUser(Databases.Main, Connection.TRANSACTION_SERIALIZABLE);
        i.getCustomersHavingInEmail("bollywood");
        you.updateCustomerEmail(Customers.AshokKumar, newEmail());
    }

    @Test
    public void no_Phantom_Even_When_The_Database_Has_Changed() throws Exception {
        final UUID uuid = UUID.randomUUID();
        i = new DatabaseUser(Databases.Main, Connection.TRANSACTION_SERIALIZABLE);
        ArrayList<Customer> customersList = i.getCustomersHavingInName("Ashok");
        you.createCustomer("Ashok Mitra", "amitra" + uuid.toString() + "@yahoo.com");
        you.commit();
        ArrayList<Customer> newCustomersList = i.getCustomersHavingInName("Ashok");
        assert customersList.size() == newCustomersList.size();
    }
}