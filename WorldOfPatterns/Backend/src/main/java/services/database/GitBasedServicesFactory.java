package services.database;

import services.patterns.GitBasedPatternsService;
import services.patterns.MySqlBasedPatternsService;
import services.patterns.PatternsService;

/**
 * Database Services Factory using MySQL services
 * This is a singleton
 */
public class GitBasedServicesFactory implements ServicesFactory
{
    protected GitBasedServicesFactory() {}

    public PatternsService createPatternsService() {
        return new GitBasedPatternsService();
    }

    public static ServicesFactory instance()
    {
        return new GitBasedServicesFactory();
    }
}
