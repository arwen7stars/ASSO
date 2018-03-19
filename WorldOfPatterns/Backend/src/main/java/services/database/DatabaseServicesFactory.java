package services.database;

import services.patterns.PatternsService;

/**
 * Database Services Factory
 * Abstract Factory is used to create different families of database services
 */
public interface DatabaseServicesFactory
{
    DatabaseService createDatabaseService();

    PatternsService createPatternsService();
}
