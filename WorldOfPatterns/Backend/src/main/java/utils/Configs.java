package utils;

import services.database.DatabaseServicesFactory;
import services.database.MySqlBasedDatabaseServicesFactory;

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

    public static String ALLOWED_ORIGINS = "http://projectidealize.me,https://projectidealize.me";

    public static DatabaseServicesFactory databaseServicesFactory;

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

    public static void setConsts(Properties prop)
    {
        databaseServicesFactory = MySqlBasedDatabaseServicesFactory.instance();

        DB_HOST = prop.getProperty("dbaddr");
        DB_PORT = prop.getProperty("dbport");
        DB_NAME = prop.getProperty("dbname");
        DB_USERNAME = prop.getProperty("dbuser");
        DB_PASSWORD = prop.getProperty("dbpass");

        ALLOWED_ORIGINS = prop.getProperty("allowed_origins");
    }
}
