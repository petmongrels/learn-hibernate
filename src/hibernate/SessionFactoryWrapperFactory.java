package hibernate;

import database.Databases;

public class SessionFactoryWrapperFactory {
    public static ISessionFactoryWrapper create(String databaseType, String databaseName){
        if (databaseType.equals("oracle")) {
            return new OracleSessionFactoryWrapper();
        } else if (databaseName.equals(Databases.Main)){
            return new SqlServerSessionFactoryWrapper();
        }
        return new AltSessionFactoryWrapper();
    }
}
