package entity;

import java.util.ArrayList;

public class Transactions extends ArrayList<Transaction> {
    public Transactions(Accounts accounts) {
        add(new Transaction(100.00, TransactionType.Debit, accounts.ashokKumarSavings(), "Pocket Money"));
        add(new Transaction(200.00, TransactionType.Debit, accounts.ashokKumarSavings(), "Credit Card"));
        add(new Transaction(300.00, TransactionType.Debit, accounts.ashokKumarSavings(), "ATM"));

        add(new Transaction(100.00, TransactionType.Debit, accounts.ashokKumarCurrent(), "Pocket Money"));
        add(new Transaction(200.00, TransactionType.Debit, accounts.ashokKumarCurrent(), "Credit Card"));
        add(new Transaction(300.00, TransactionType.Debit, accounts.ashokKumarCurrent(), "ATM"));
    }
}
