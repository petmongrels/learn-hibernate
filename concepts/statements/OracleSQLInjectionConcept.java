package statements;

import configuration.AppConfigurationImpl;
import configuration.DatabaseSettings;
import configuration.OracleSettings;
import database.DatabaseConnection;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class OracleSQLInjectionConcept {
    private DatabaseConnection connection;

    @BeforeMethod
    public void setUp() throws Exception {
        final DatabaseSettings databaseSettings = new OracleSettings(new AppConfigurationImpl());
        connection = new DatabaseConnection(databaseSettings);
        connection.beginTransaction();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        connection.rollback();
        connection.close();
    }

    @Test
    public void executing_Another_Command_Not_Possible_In_Oracle() throws Exception {
        try {
            connection.queryRows("select Id from Customers where Name = '" + getNameWithAnotherStatementAppended() + "'");
            assert false;
        } catch (SQLException ignored) {
        }
    }

    @Test
    public void loading_More_Rows() throws Exception {
        final ArrayList<Object[]> rows = connection.queryRows("select Id from Customers where Name = '" + getNameWithExtraClause() + "'");
        assert rows.size() > 1;
    }

    private String getNameWithAnotherStatementAppended() {
        return "Ashok Kumar';delete from Transactions--";
    }

    private String getNameWithExtraClause() {
        return "Ashok Kumar' or '1' = '1";
    }

    @Test
    public void execute_Using_Prepared_Statement() throws Exception {
        ArrayList<Object[]> customers = connection.queryRows("select Id from Customers where Name = ?", getNameWithExtraClause());
        assert customers.size() == 0;
        ArrayList<Object[]> transactions = connection.queryRows("select Id from Transactions");
        assert transactions.size() != 0;
    }
}