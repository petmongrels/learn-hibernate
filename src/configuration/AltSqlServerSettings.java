package configuration;

import database.Databases;

public class AltSqlServerSettings extends BaseSqlServerSettings {
    public AltSqlServerSettings(AppConfiguration appConfiguration) {
        super(appConfiguration);
    }

    protected String databaseName() {
        return Databases.Alt;
    }
}