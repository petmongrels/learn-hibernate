package configuration;

public interface DatabaseSettings {
    String driverClass();
    String dialect();
    String url(AppConfiguration appConfiguration);
    String user(AppConfiguration appConfiguration);
    String password(AppConfiguration appConfiguration);
}
