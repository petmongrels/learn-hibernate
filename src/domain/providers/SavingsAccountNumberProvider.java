package domain.providers;

import domain.providers.AccountNumberProvider;

import java.util.Calendar;

public class SavingsAccountNumberProvider implements AccountNumberProvider {
    public String newAccount() {
        return "SB" + Calendar.getInstance().getTimeInMillis();
    }
}
