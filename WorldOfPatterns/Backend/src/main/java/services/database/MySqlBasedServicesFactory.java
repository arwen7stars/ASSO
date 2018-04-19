package services.database;

import services.patterns.MySqlBasedPatternsService;
import services.patterns.PatternsService;

/**
 * Database Services Factory using MySQL services
 * This is a singleton
 */
public class MySqlBasedServicesFactory
{
    protected MySqlBasedServicesFactory() {}

    public DatabaseService createDatabaseService() {
        return new MySqlBasedDatabaseService();
    }

    public MySqlBasedPatternsService createPatternsService() {
        return new MySqlBasedPatternsService();
    }

    public static MySqlBasedServicesFactory instance()
    {
        return new MySqlBasedServicesFactory();
    }
}
