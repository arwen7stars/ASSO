package services.patterns;

import patterns.Pattern;
import services.database.DatabaseService;
import services.database.MySqlBasedDatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Patterns Service using MySQL
 */
public class MySqlBasedPatternsService implements PatternsService
{
    //Queries
    private static final String CREATE_PATTERN_QUERY = "insert into Pattern(name) values(?);";
    private static final String GET_PATTERNS_QUERY = "select * from Pattern;";

    //Failure messages
    private static final String CREATE_PATTERN_FAILED = "Could not create pattern";
    private static final String GET_PATTERNS_FAILED = "Could not select all patterns";

    //Column names
    private static final String NAME = "name";

    //Database object
    private DatabaseService database;

    //Constructor
    public MySqlBasedPatternsService()
    {
        database = new MySqlBasedDatabaseService();
    }

    //Create a Pattern
    public boolean createPattern(String name)
    {
        Connection con = database.getConnection();

        boolean res = false;

        try{
            PreparedStatement stmt = con.prepareStatement(CREATE_PATTERN_QUERY);
            stmt.setString(1, name);
            stmt.execute();
        }
        catch(Exception e){
            System.out.println(CREATE_PATTERN_FAILED);
            res = false;
        }

        database.closeConnection(con);

        return res;
    }

    //Get all patterns
    public ArrayList<Pattern> getPatterns()
    {
        Connection con = database.getConnection();

        ArrayList<Pattern> patterns = new ArrayList<>();

        try{
            PreparedStatement stmt = con.prepareStatement(GET_PATTERNS_QUERY);
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                patterns.add(new Pattern(rs.getString(NAME)));
            }
        }
        catch(Exception e){
            System.out.println(GET_PATTERNS_FAILED);
        }

        database.closeConnection(con);

        return patterns;
    }
}
