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
    private static GitBasedServicesFactory factory;

    protected GitBasedServicesFactory() {}

    public PatternsService createPatternsService() {
        return new GitBasedPatternsService();
    }

    public static ServicesFactory instance()
    {
        if(factory == null) {
            factory = new GitBasedServicesFactory();
        }

        return factory;
    }
}
