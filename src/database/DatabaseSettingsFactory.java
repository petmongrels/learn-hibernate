package database;

import configuration.*;

public class DatabaseSettingsFactory {
    public static String Oracle = "oracle";

    public static DatabaseSettings create(AppConfiguration appConfiguration) {
        return create(appConfiguration, Databases.Main);
    }

    public static DatabaseSettings create(AppConfiguration appConfiguration, String databaseName) {
        return create(appConfiguration, databaseName, appConfiguration.activeDatabase());
    }

    public static DatabaseSettings create(AppConfiguration appConfiguration, String databaseName, String activeDatabase) {
        if (activeDatabase.equalsIgnoreCase(Oracle)) {
            new OracleSettings(appConfiguration);
        } else if (databaseName.equals(Databases.Main)) {
            return new SqlServerSettings(appConfiguration);
        }
        return new AltSqlServerSettings(appConfiguration);
    }
}
