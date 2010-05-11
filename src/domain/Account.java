package domain;

import java.math.BigDecimal;
import java.util.List;

public class Account {
    private int id;
    private BigDecimal balance;
    private String accountNumber;
    private List<BankTransaction> transactions;

    protected Account() {
    }

    public Account(int id, BigDecimal balance, String accountNumber) {
        this.id = id;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    public BankTransaction withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);
        return new BankTransaction(this, amount, TransactionType.Debit);
    }

    public int getId() {
        return id;
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
}
