package configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfigurationImpl implements AppConfiguration {
    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("app.properties"));
            AppConfigurationImpl.properties = properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Properties properties;

    public String sqlServerUser(){
        return properties.getProperty("sqlserver.user"); 
    }

    public String sqlServerPassword(){
        return properties.getProperty("sqlserver.password"); 
    }

    public int queryTimeoutInSeconds() {
        return new Integer(properties.getProperty("query.timeout.in.secs"));
    }

    public String sqlServer() {
        return properties.getProperty("sqlserver");
    }

    public String oraclePassword() {
        return properties.getProperty("oracle.password");
    }
}
