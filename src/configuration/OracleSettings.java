package configuration;

import oracle.jdbc.driver.OracleDriver;
import org.hibernate.dialect.Oracle10gDialect;

public class OracleSettings implements DatabaseSettings {
    public String driverClass() {
        return OracleDriver.class.getName();
    }

    public String dialect() {
        return Oracle10gDialect.class.getName();
    }

    public String url(AppConfiguration appConfiguration) {
        return "jdbc:oracle:thin:@localhost:1521:XE";
    }

    public String user(AppConfiguration appConfiguration) {
        return "LearnHibernate";
    }

    public String password(AppConfiguration appConfiguration) {
        return appConfiguration.oraclePassword();
    }
}
