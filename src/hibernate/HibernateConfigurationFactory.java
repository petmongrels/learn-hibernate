package hibernate;

import configuration.AppConfigurationImpl;
import database.Databases;
import org.hibernate.cache.NoCacheProvider;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.SQLServerDialect;

public class HibernateConfigurationFactory {
    static Configuration createBasicConfiguration() {
        AppConfigurationImpl appConfiguration = new AppConfigurationImpl();
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", net.sourceforge.jtds.jdbc.Driver.class.getName());
        configuration.setProperty("hibernate.connection.dialect", SQLServerDialect.class.getName());
        configuration.setProperty("hibernate.connection.url", "jdbc:jtds:sqlserver://" + appConfiguration.sqlServer() + ":1433/" + Databases.Main + ";SelectMethod=cursor");
        configuration.setProperty("hibernate.connection.username", appConfiguration.sqlServerUser());
        configuration.setProperty("hibernate.connection.password", appConfiguration.sqlServerPassword());
        configuration.setProperty("hibernate.connection.pool_size", "1");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("hibernate.cache.provider_class", NoCacheProvider.class.getName());
        configuration.setProperty("hibernate.show_sql", "true");
        return configuration;
    }
}