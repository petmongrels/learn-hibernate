package hibernate;

import configuration.DatabaseSettings;
import org.hibernate.cache.HashtableCacheProvider;
import org.hibernate.cfg.Configuration;

public class HibernateConfigurationFactory {
    static Configuration createBasicConfiguration(DatabaseSettings databaseSettings) {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", databaseSettings.driverClass());
        configuration.setProperty("hibernate.connection.dialect", databaseSettings.dialect());
        configuration.setProperty("hibernate.connection.url", databaseSettings.url());
        configuration.setProperty("hibernate.connection.username", databaseSettings.user());
        configuration.setProperty("hibernate.connection.password", databaseSettings.password());
        configuration.setProperty("hibernate.connection.pool_size", "1");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("hibernate.cache.use_second_level_cache", "true");
        configuration.setProperty("hibernate.cache.provider_class", HashtableCacheProvider.class.getName());
        configuration.setProperty("hibernate.show_sql", "true");
        return configuration;
    }
}
