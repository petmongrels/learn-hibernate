package configuration;

import oracle.jdbc.driver.OracleDriver;
import org.hibernate.dialect.Oracle10gDialect;

public class OracleSettings implements DatabaseSettings {
    private AppConfiguration appConfiguration;

    public OracleSettings() {
        this(new AppConfigurationImpl());
    }

    public OracleSettings(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    public String driverClass() {
        return OracleDriver.class.getName();
    }

    public String dialect() {
        return Oracle10gDialect.class.getName();
    }

    public String url() {
        return "jdbc:oracle:thin:@localhost:1521:XE";
    }

    public String user() {
        return "LearnHibernate";
    }

    public String password() {
        return appConfiguration.oraclePassword();
    }

    public int queryTimeOut() {
        return appConfiguration.queryTimeoutInSeconds();
    }
}
