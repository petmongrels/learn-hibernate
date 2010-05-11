package domain;

import java.math.BigDecimal;
import java.util.List;

public class Customer {
    private int id;
    private String name;
    private String email;
    private int version;
    private List<Account> accounts;

    protected Customer() {
    }

    public Customer(int id, String name, String email, int version) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public String getEmail() {
        return email;
    }

    public int numberOfAccounts(){
        return accounts.size();
    }

    public BigDecimal totalBalance() {
        BigDecimal totalBalance = new BigDecimal(0);
        for(Account account : accounts) {
            totalBalance = totalBalance.add(account.getBalance());
        }
        return totalBalance;
    }

    public void totalTransactedAmount() {
        BigDecimal transactedAmount = new BigDecimal(0);        
         for(Account account : accounts) {
            transactedAmount.add(account.totalTransactedAmount());
        }
    }
}
