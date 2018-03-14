package services.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;

import static utils.Configs.*;

/**
 * Database Service using MySQL
 * Used to get connections
 */
public class MySqlBasedDatabaseService implements DatabaseService
{
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://";
    private static final String USE_SSL = "?useSSL=true";
    private static final String SEPARATOR = "/";
    private static final String HOST_PORT_SEPARATOR = ":";
    private static final int MAX_STATEMENTS = 180;
    private static final int TEST_TIME = 4*60*60;
    private static final int CHECKOUT_TIMEOUT = 5000;

    private ComboPooledDataSource cpds;

    private static final String CONNECTION_FAILED = "Failed to connect to MySQL";
    private static final String CONNECTION_CLOSURE_FAILED = "Failed to close connection to MySQL";

    public MySqlBasedDatabaseService()
    {
        setup();
    }

    public void finalize()
    {
        close();
    }

    public void setup()
    {
        try
        {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(DRIVER_NAME); //loads the jdbc driver
            cpds.setJdbcUrl(DB_URL + DB_HOST + HOST_PORT_SEPARATOR + DB_PORT + SEPARATOR + DB_NAME + USE_SSL);
            cpds.setUser(DB_USERNAME);
            cpds.setPassword(DB_PASSWORD);
            cpds.setMaxStatements(MAX_STATEMENTS);
            cpds.setIdleConnectionTestPeriod(TEST_TIME);
            cpds.setCheckoutTimeout(CHECKOUT_TIMEOUT);
            Connection test = cpds.getConnection();
            test.close();
        }
        catch(Exception e)
        {
            System.out.println(CONNECTION_FAILED + e.getMessage());
            System.exit(1);
        }
    }

    public void close()
    {
        try
        {
            cpds.close();
        }
        catch(Exception e)
        {
            System.out.println(CONNECTION_CLOSURE_FAILED);
        }
    }

    public Connection getConnection()
    {
        try
        {
            return cpds.getConnection();
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage());
        }

        return null;
    }

    public boolean closeConnection(Connection con)
    {
        if(con == null)
        {
            return false;
        }

        try
        {
            con.close();
            return true;
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage());
        }

        return false;
    }
}
