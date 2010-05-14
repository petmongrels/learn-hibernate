package domain;

import java.math.BigDecimal;

public class BankTransaction {
    private int id;
    private Account account;
    private BigDecimal amount;
    private TransactionType transactionType;

    private BankTransaction() {
    }

    public BankTransaction(Account account, BigDecimal amount, TransactionType transactionType) {
        this.account = account;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public BankTransaction(BankTransaction source) {
        this(source.account, source.amount, source.transactionType);
        id = source.id;
    }

    public Account getAccount() {
        return account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public BankTransaction copy() {
        return new BankTransaction(this);
    }
}
