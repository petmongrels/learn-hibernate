package domain;

import java.math.BigDecimal;

public class BankTransaction {
    private Account account;
    private BigDecimal amount;
    private TransactionType transactionType;

    public BankTransaction(Account account, BigDecimal amount, TransactionType transactionType) {
        this.account = account;
        this.amount = amount;
        this.transactionType = transactionType;
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
}
