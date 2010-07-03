package database;

public interface DatabaseProperties {
    String connectionURL(String databaseName);
    String driverName();
    String user();
    String password();
    int queryTimeOut();
}
