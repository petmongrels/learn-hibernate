package configuration;

import org.hibernate.dialect.SQLServerDialect;

public abstract class BaseSqlServerSettings implements DatabaseSettings {
    protected AppConfiguration appConfiguration;

    protected BaseSqlServerSettings(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    public String driverClass() {
        return net.sourceforge.jtds.jdbc.Driver.class.getName();
    }

    public String dialect() {
        return SQLServerDialect.class.getName();
    }

    public String url() {
        return "jdbc:jtds:sqlserver://" + appConfiguration.sqlServer() + ":1433/" + databaseName() + ";SelectMethod=cursor";
    }

    protected abstract String databaseName();

    public String user() {
        return appConfiguration.sqlServerUser();
    }

    public String password() {
        return appConfiguration.sqlServerPassword();
    }

    public int queryTimeOut() {
        return appConfiguration.queryTimeoutInSeconds();
    }
}
