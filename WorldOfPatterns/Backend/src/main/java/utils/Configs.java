package utils;

import services.database.GitBasedServicesFactory;
import services.database.ServicesFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configurations are stored here
 */
public class Configs
{
    public static String CONFIG_PATH;

    public static String DB_HOST;
    public static String DB_PORT;
    public static String DB_NAME;
    public static String DB_USERNAME;
    public static String DB_PASSWORD;

    public static String GIT_USER;
    public static String GIT_PASSWORD;
    public static String GIT_EMAIL;
    public static String GIT_REPOSITORY;
    public static String REPOSITORY_PATH;

    public static String ALLOWED_ORIGINS;

    public static boolean PULL_FROM_GIT = false;

    public static ServicesFactory servicesFactory;

    /**
     * Reads configuration file
     * @param path Path to the configuration file
     */
    public static void readConfig(String path)
    {
        CONFIG_PATH = path;

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream(CONFIG_PATH);

            prop.load(input);

            setConsts(prop);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Set global constants
     * @param prop Properties object
     */
    public static void setConsts(Properties prop)
    {
        servicesFactory = GitBasedServicesFactory.instance();

        DB_HOST = prop.getProperty("dbaddr");
        DB_PORT = prop.getProperty("dbport");
        DB_NAME = prop.getProperty("dbname");
        DB_USERNAME = prop.getProperty("dbuser");
        DB_PASSWORD = prop.getProperty("dbpass");

        GIT_USER = prop.getProperty("gituser");
        GIT_PASSWORD = prop.getProperty("gitpassword");
        GIT_EMAIL = prop.getProperty("gitemail");
        GIT_REPOSITORY = prop.getProperty("gitrepository");
        REPOSITORY_PATH = prop.getProperty("repositorypath");

        ALLOWED_ORIGINS = prop.getProperty("allowed_origins");

        PULL_FROM_GIT = Boolean.parseBoolean(prop.getProperty("pull_from_git"));
    }
}
