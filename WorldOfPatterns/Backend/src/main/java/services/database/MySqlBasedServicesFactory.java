package services.database;

import services.patterns.MySqlBasedPatternsService;
import services.patterns.PatternsService;

/**
 * Database Services Factory using MySQL services
 * This is a singleton
 */
public class MySqlBasedServicesFactory implements ServicesFactory
{
    protected MySqlBasedServicesFactory() {}

    public DatabaseService createDatabaseService() {
        return new MySqlBasedDatabaseService();
    }

    public PatternsService createPatternsService() {
        return new MySqlBasedPatternsService();
    }

    public static ServicesFactory instance()
    {
        return new MySqlBasedServicesFactory();
    }
}
