package services.database;

import java.sql.Connection;

public interface DatabaseService
{
    void setup();

    Connection getConnection();

    void close();

    boolean closeConnection(Connection connection);
}
