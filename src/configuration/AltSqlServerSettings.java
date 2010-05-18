package configuration;

import database.Databases;

public class AltSqlServerSettings extends BaseSqlServerSettings {
    protected String databaseName() {
        return Databases.Alt;
    }
}