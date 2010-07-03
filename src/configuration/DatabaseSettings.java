package configuration;

public interface DatabaseSettings {
    String driverClass();
    String dialect();
    String url();
    String user();
    String password();
    int queryTimeOut();
}
