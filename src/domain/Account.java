package domain;

import java.math.BigDecimal;

public class Account {
    private int id;
    private BigDecimal balance;
    private String accountNumber;

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
}
