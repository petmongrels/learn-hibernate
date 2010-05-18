package domain;

import domain.providers.AccountNumberProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Entity {
    private String name;
    private String email;
    private List<Account> accounts;
    private City city;
    private List<Address> addresses;

    protected Customer() {
    }

    public Customer(int id, String name, String email, int version) {
        super(id, version);
        this.name = name;
        this.email = email;
    }

    public Customer(Customer customer) {
        this(customer.getId(), customer.name, customer.email, customer.version);
        city = customer.city.copy();
        version = customer.version;
    }

    public int getVersion() {
        return version;
    }

    public String getEmail() {
        return email;
    }

    public int numberOfAccounts() {
        return accounts.size();
    }

    public BigDecimal totalBalance() {
        BigDecimal totalBalance = new BigDecimal(0);
        for (Account account : accounts) {
            totalBalance = totalBalance.add(account.getBalance());
        }
        return totalBalance;
    }

    public BigDecimal totalTransactedAmount() {
        BigDecimal transactedAmount = new BigDecimal(0);
        for (Account account : accounts) {
            transactedAmount = transactedAmount.add(account.totalTransactedAmount());
        }
        return transactedAmount;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public City getCity() {
        return city;
    }

    @Deprecated
    public void setVersion(int version) {
        this.version = version;
    }

    public Customer copy() {
        Customer customer = new Customer(this);
        customer.accounts = new ArrayList<Account>();
        for(Account account : accounts) {
            customer.accounts.add(account.copy());
        }
        customer.addresses = new ArrayList<Address>();
        for(Address address : addresses) {
            customer.addresses.add(address.copy());
        }
        return customer;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Address[] getAddresses() {
        return addresses.toArray(new Address[addresses.size()]);
    }

    public void clearAndApplyNewAddresses(ArrayList<Address> lastestAddresses) {
        this.addresses.clear();
        this.addresses.addAll(lastestAddresses);
    }

    public void replaceAddresses(ArrayList<Address> lastestAddresses) {
        this.addresses = new ArrayList<Address>();
        this.addresses.addAll(lastestAddresses);
    }

    public void editAddresses(ArrayList<Address> editedAddresses) {
        PersistedCollectionEditor<Address> editor = new PersistedCollectionEditor<Address>(addresses);
        editor.edit(editedAddresses);
    }

    public void newAccount(BigDecimal startingAmount, AccountNumberProvider accountNumberProvider) {
        final Account account = Account.NewAccount(this, startingAmount, accountNumberProvider);
        accounts.add(account);
    }
}
