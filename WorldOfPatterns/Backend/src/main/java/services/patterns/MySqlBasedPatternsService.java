package services.patterns;

import patterns.Pattern;
import services.database.DatabaseService;
import services.database.MySqlBasedDatabaseService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Patterns Service using MySQL
 */
public class MySqlBasedPatternsService
{
    //Queries
    private static final String CREATE_PATTERN_QUERY = "insert into pattern(name) values(?);";
    private static final String GET_LAST_PATTERN_ID = "SELECT LAST_INSERT_ID() AS id;";
    private static final String GET_PATTERNS_QUERY = "select * from pattern;";
    private static final String GET_PATTERN_QUERY = "select * from pattern where id = ?;";
    private static final String DELETE_PATTERN_QUERY = "delete * from where id = ?";

    //Failure messages
    private static final String CREATE_PATTERN_FAILED = "Could not create pattern";
    private static final String GET_PATTERNS_FAILED = "Could not select all patterns";

    //Column names
    private static final String ID = "id";
    private static final String NAME = "name";

    //Database object
    private DatabaseService database;

    //Constructor
    public MySqlBasedPatternsService()
    {
        database = new MySqlBasedDatabaseService();
    }

    //Create a Pattern
    public int createPattern(String name) throws Exception
    {
        int res;

        Connection con = database.getConnection();

        PreparedStatement stmt = con.prepareStatement(CREATE_PATTERN_QUERY);
        stmt.setString(1, name);
        stmt.execute();

        PreparedStatement stmt2 = con.prepareStatement(GET_LAST_PATTERN_ID);
        ResultSet rs = stmt2.executeQuery();

        if(rs.next()) {
            res = rs.getInt(ID);
        }
        else {
            throw new Exception();
        }

        database.closeConnection(con);

        return res;
    }

    //Get all patterns
    public ArrayList<Pattern> getPatterns() throws SQLException
    {
        Connection con = database.getConnection();

        ArrayList<Pattern> patterns = new ArrayList<>();

        PreparedStatement stmt = con.prepareStatement(GET_PATTERNS_QUERY);
        ResultSet rs = stmt.executeQuery();

        while(rs.next())
        {
            patterns.add(new Pattern(rs.getInt(ID), rs.getString(NAME)));
        }

        database.closeConnection(con);

        return patterns;
    }

    public Pattern getPattern(int id) throws SQLException {
        Connection con = database.getConnection();

        Pattern pattern = null;

        PreparedStatement stmt = con.prepareStatement(GET_PATTERN_QUERY);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
        {
            pattern = new Pattern(rs.getInt(ID), rs.getString(NAME));
        }

        database.closeConnection(con);

        return pattern;
    }

    public void rollbackChanges(){
        Connection con = database.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(GET_LAST_PATTERN_ID);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                int id = rs.getInt(ID);

                PreparedStatement stmt2 = con.prepareStatement(DELETE_PATTERN_QUERY);
                stmt2.setInt(1, id);
                stmt2.execute();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        database.closeConnection(con);
    }
}
