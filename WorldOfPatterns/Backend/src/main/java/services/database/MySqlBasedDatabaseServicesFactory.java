package services.database;

import services.patterns.MySqlBasedPatternsService;
import services.patterns.PatternsService;

/**
 * Database Services Factory using MySQL services
 * This is a singleton
 */
public class MySqlBasedDatabaseServicesFactory implements DatabaseServicesFactory
{
    protected MySqlBasedDatabaseServicesFactory() {}

    public DatabaseService createDatabaseService() {
        return new MySqlBasedDatabaseService();
    }

    public PatternsService createPatternsService() {
        return new MySqlBasedPatternsService();
    }

    public static DatabaseServicesFactory instance()
    {
        return new MySqlBasedDatabaseServicesFactory();
    }
}
