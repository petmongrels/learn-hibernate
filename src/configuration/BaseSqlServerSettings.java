package configuration;

import org.hibernate.dialect.SQLServerDialect;

public abstract class BaseSqlServerSettings implements DatabaseSettings {
    public String driverClass() {
        return net.sourceforge.jtds.jdbc.Driver.class.getName();
    }

    public String dialect() {
        return SQLServerDialect.class.getName();
    }

    public String url(AppConfiguration appConfiguration) {
        return "jdbc:jtds:sqlserver://" + appConfiguration.sqlServer() + ":1433/" + databaseName() + ";SelectMethod=cursor";
    }

    protected abstract String databaseName();

    public String user(AppConfiguration appConfiguration) {
        return appConfiguration.sqlServerUser();
    }

    public String password(AppConfiguration appConfiguration) {
        return appConfiguration.sqlServerPassword();
    }
}
