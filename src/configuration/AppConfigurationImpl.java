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

    public String SqlServerUser(){
        return properties.getProperty("sqlserver.user"); 
    }

    public String SqlServerPassword(){
        return properties.getProperty("sqlserver.password"); 
    }
}
