import configuration.*;
import database.DatabaseConnection;
import entity.*;

public class TestDataCreator {
    public static void main(String[] args) throws Exception {
        final AppConfigurationImpl appConfiguration = new AppConfigurationImpl();
        final DatabaseSettings sqlServerSettings = new SqlServerSettings(appConfiguration);
        final DatabaseSettings altSqlServerSettings = new AltSqlServerSettings(appConfiguration);
        final DatabaseSettings oracleSettings = new OracleSettings(appConfiguration);

        final Cities allCities = new Cities();
        final Customers allCustomers = new Customers(allCities);
        final Accounts allAccounts = new Accounts(allCustomers);
        final Addresses allAddresses = new Addresses(allCustomers);

        if (args.length >= 1 && args[0].equals("sqlserver")) {
            populateTestData(sqlServerSettings, allCities, allCustomers, allAccounts, allAddresses);
            populateTestData(altSqlServerSettings, allCities, allCustomers, allAccounts, allAddresses);
        } else {
            populateTestData(oracleSettings, allCities, allCustomers, allAccounts, allAddresses);
        }
    }

    private static void populateTestData(DatabaseSettings databaseSettings, Cities allCities, Customers allCustomers, Accounts allAccounts, Addresses allAddresses) throws Exception {
        final DatabaseConnection databaseConnection = new DatabaseConnection(databaseSettings);
        createCities(databaseConnection, allCities);
        createCustomers(databaseConnection, allCustomers);
        createAccounts(databaseConnection, allAccounts);
        createTransactions(databaseConnection, allAccounts);
        createAddresses(databaseConnection, allAddresses);
    }

    private static void createAddresses(DatabaseConnection databaseConnection, Addresses allAddresses) throws Exception {
        for (Address address : allAddresses) {
            final Object customerId = databaseConnection.queryValue("select Id from Customers where Name = ?", address.getCustomer().getName());
            databaseConnection.execute("insert into Addresses (Line1, Line2, CustomerId) values (?, ?, ?)",
                    address.getLine1(), address.getLine2(), customerId);
        }
    }

    private static void createTransactions(DatabaseConnection databaseConnection, Accounts allAccounts) throws Exception {
        for (Transaction transaction : new Transactions(allAccounts)) {
            final Object accountId = databaseConnection.queryValue("select Id from Accounts where AccountNumber = ?", transaction.getAccount().getNumber());
            databaseConnection.execute("insert into Transactions (Amount, Type, AccountId, Description) values (?, ?, ?, ?)",
                    transaction.getAmount(), transaction.getType().toString(), accountId, transaction.getDescription());
        }
    }

    private static void createAccounts(DatabaseConnection databaseConnection, Accounts allAccounts) throws Exception {
        for (Account account : allAccounts) {
            final Object customerId = databaseConnection.queryValue("select Id from Customers where Name = ?", account.getCustomer().getName());
            databaseConnection.execute("insert into Accounts (AccountNumber, CustomerId, Balance) values (?, ?, ?)",
                    account.getNumber(), customerId, account.getBalance());
        }
    }

    private static void createCustomers(DatabaseConnection databaseConnection, Customers allCustomers) throws Exception {
        for (Customer customer : allCustomers) {
            Object cityId = databaseConnection.queryValue("select Id from Cities where Name = ?", customer.getCity().getName());
            databaseConnection.execute("insert into Customers (Name, Email, CityId) values (?, ?, ?)",
                    customer.getName(), customer.getEmail(), cityId);
        }
    }

    private static void createCities(DatabaseConnection databaseConnection, Cities allCities) throws Exception {
        for (City city : allCities)
            databaseConnection.execute("insert into Cities (Name) values (?)", city.getName());
    }
}
