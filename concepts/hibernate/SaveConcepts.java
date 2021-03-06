package hibernate;

import configuration.AppConfigurationImpl;
import database.Databases;
import domain.Account;
import domain.Address;
import domain.BankTransaction;
import domain.Customer;
import dto.AddressDTO;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueObjectException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SaveConcepts extends HibernateConceptBase {
    private Customer changedCustomer;

    protected ISessionFactoryWrapper sessionFactoryWrapper() {
        return SessionFactoryWrapperFactory.create(new AppConfigurationImpl().activeDatabase(), Databases.Main);
    }

    @BeforeMethod
    @Override
    public void setUp() {
        super.setUp();
        Customer customer = (Customer) session.load(Customer.class, 1);
        changedCustomer = customer.copy();
        session.clear();
        sessionFactory.getStatistics().clear();
        System.out.println("SetUp Complete");
    }

    @Test
    public void save_Or_Update_Disconnected_Object__Updates_The_Entire_Graph() {
        Account account = changedCustomer.getAccounts().get(0);
        account.withdraw(new BigDecimal(100));
        session.saveOrUpdate(account);
        session.flush();

        assert 1 == updateCount(Account.class);
        assert 1 == insertCount(BankTransaction.class);
        assert account.transactionCount() - 1 == updateCount(BankTransaction.class);
    }

    @Test
    public void merge_When_Entire_Graph_Is_Edited() {
        Account account = changedCustomer.getAccounts().get(0);
        account.withdraw(new BigDecimal(100));
        session.merge(account);
        session.flush();

        assert 1 == updateCount(Account.class);
        assert 0 == updateCount(BankTransaction.class);
        assert 1 == insertCount(BankTransaction.class);
        assert 4 == loadCount();
    }

    @Test
    public void do_Not_Clear_Child_Collection() {
        ArrayList<AddressDTO> addressDTOs = addressRead();
        addNewAddress(addressDTOs);
        Customer customer = (Customer) session.load(Customer.class, 1);
        ArrayList<Address> editedAddresses = mapAddresses(addressDTOs, customer);
        customer.clearAndApplyNewAddresses(editedAddresses);
        try {
            session.flush();
            assert false;
        } catch (NonUniqueObjectException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void donot_Replace_Collection() {
        ArrayList<AddressDTO> addressDTOs = addressRead();
        addNewAddress(addressDTOs);
        Customer customer = (Customer) session.load(Customer.class, 1);
        ArrayList<Address> editedAddresses = mapAddresses(addressDTOs, customer);
        customer.replaceAddresses(editedAddresses);
        try {
            session.flush();
            assert false;
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void edit_Collections_When_Not_Updating_The_Entire_Graph() {
        ArrayList<AddressDTO> addressDTOs = addressRead();
        addNewAddress(addressDTOs);
        Customer customer = (Customer) session.load(Customer.class, 1);
        ArrayList<Address> editedAddresses = mapAddresses(addressDTOs, customer);
        customer.editAddresses(editedAddresses);
        assert customer.getAddresses().length == 3;
        session.flush();

        assert 1 == updateCount(Customer.class);
        assert 0 == updateCount(Address.class);
        assert 1 == insertCount(Address.class);        
    }

    private ArrayList<Address> mapAddresses(ArrayList<AddressDTO> addressDTOs, Customer customer) {
        ArrayList<Address> editedAddresses = new ArrayList<Address>();
        for (AddressDTO addressDTO : addressDTOs) {
            editedAddresses.add(new Address(addressDTO.getId(), addressDTO.getLine1(), addressDTO.getLine2(), customer));
        }
        return editedAddresses;
    }

    private void addNewAddress(ArrayList<AddressDTO> addressDTOs) {
        addressDTOs.add(new AddressDTO(0, "Venugopal Layout", "Anand Nagar"));
    }

    private ArrayList<AddressDTO> addressRead() {
        ArrayList<AddressDTO> addressData = new ArrayList<AddressDTO>();
        Address[] addresses = changedCustomer.getAddresses();
        for (Address address : addresses) {
            addressData.add(new AddressDTO(address.getId(), address.getLine1(), address.getLine2()));
        }
        return addressData;
    }
}
