package domain;

import java.math.BigDecimal;

public class BankTransaction {
    private int id;
    private Account account;
    private BigDecimal amount;
    private String type;

    private BankTransaction() {
    }

    public BankTransaction(Account account, BigDecimal amount, String type) {
        this.account = account;
        this.amount = amount;
        this.type = type;
    }

    public BankTransaction(BankTransaction source) {
        this(source.account, source.amount, source.type);
        id = source.id;
    }

    public Account getAccount() {
        return account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public BankTransaction copy() {
        return new BankTransaction(this);
    }
}
