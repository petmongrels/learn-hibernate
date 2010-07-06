package entity;

import java.util.ArrayList;

public class Accounts extends ArrayList<Account> {
    private String ashokKumarSavingAccountNumber = "SB12345678";
    private Customers customers;

    public Accounts(Customers customers) {
        this.customers = customers;
        add(ashokKumarSavings());
        add(ashokKumarCurrent());
    }

    public Account ashokKumarSavings() {
        return new Account(ashokKumarSavingAccountNumber, customers.ashokKumar(), 200000.00);
    }

    public Account ashokKumarCurrent() {
        return new Account("CA4234789", customers.ashokKumar(), 7000.00);
    }
}
