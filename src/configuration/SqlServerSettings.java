package configuration;

import database.Databases;

public class SqlServerSettings extends BaseSqlServerSettings {
    protected String databaseName() {
        return Databases.Main;
    }
}
