package entity;

public class Transaction {
    private double amount;
    private TransactionType type;
    private Account account;
    private String description;

    public Transaction(double amount, TransactionType type, Account account, String description) {
        this.amount = amount;
        this.type = type;
        this.account = account;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Account getAccount() {
        return account;
    }

    public String getDescription() {
        return description;
    }
}
