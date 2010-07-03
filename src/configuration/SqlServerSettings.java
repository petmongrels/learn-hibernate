package configuration;

import database.Databases;

public class SqlServerSettings extends BaseSqlServerSettings {
    public SqlServerSettings(AppConfiguration appConfiguration) {
        super(appConfiguration);
    }

    protected String databaseName() {
        return Databases.Main;
    }
}
