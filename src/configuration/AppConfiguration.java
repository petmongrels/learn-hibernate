package configuration;

public interface AppConfiguration {
    String sqlServerUser();
    String sqlServerPassword();
    int queryTimeoutInSeconds();
    String sqlServer();
}
