package domain;

import domain.providers.AccountNumberProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account extends Entity {
    private BigDecimal balance;
    private String number;
    private List<BankTransaction> transactions;
    private Customer customer;

    protected Account() {
    }

    public Account(Customer customer, int id, BigDecimal balance, String number) {
        super(id);
        this.customer = customer;
        this.balance = balance;
        this.number = number;
        transactions = new ArrayList<BankTransaction>();
    }

    public static Account NewAccount(Customer customer, BigDecimal balance, AccountNumberProvider accountNumberProvider){
        Account account = new Account(customer, 0, balance, accountNumberProvider.newAccount());
        account.transactions.add(new BankTransaction(account, balance, TransactionType.Credit));
        return account;
    }

    public Account(Customer customer, BigDecimal balance, String number) {
        this(customer, 0, balance, number);
    }

    public Account(Customer customer, Account account) {
        this(customer, account.getId(), account.balance, account.number);
    }

    public BankTransaction withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);
        BankTransaction bankTransaction = new BankTransaction(this, amount, TransactionType.Debit);
        transactions.add(bankTransaction);
        return bankTransaction;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal totalTransactedAmount() {
        BigDecimal transactedAmount = new BigDecimal(0);        
        for (BankTransaction transaction : transactions) {
            transactedAmount = transactedAmount.add(transaction.getAmount());
        }
        return transactedAmount;
    }

    public Account copy() {
        Account copy = new Account(customer, this);
        copy.transactions = new ArrayList<BankTransaction>();
        for(BankTransaction bankTransaction : transactions) {
            copy.transactions.add(bankTransaction.copy());
        }
        return copy;
    }

    public int transactionCount() {
        return transactions.size();
    }
}
