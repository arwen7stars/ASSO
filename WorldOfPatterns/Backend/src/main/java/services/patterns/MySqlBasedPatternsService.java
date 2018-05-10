package services.patterns;

import patterns.Pattern;
import patterns.PatternLanguage;
import services.database.DatabaseService;
import services.database.MySqlBasedDatabaseService;

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
    private static final String CREATE_PATTERN_WITH_ID_QUERY = "insert into pattern(id,name) values(?,?);";
    private static final String GET_LAST_INSERTED_ID = "SELECT LAST_INSERT_ID() AS id;";
    private static final String GET_PATTERNS_QUERY = "select * from pattern;";
    private static final String GET_PATTERN_QUERY = "select * from pattern where id = ?;";
    private static final String DELETE_PATTERN_QUERY = "delete from pattern where id = ?";

    private static final String CREATE_PATTERN_LANGUAGE_QUERY = "insert into patternLanguage(name) values(?);";
    private static final String GET_PATTERN_LANGUAGE_QUERY = "select * from patternLanguage where id = ?;";
    private static final String GET_PATTERN_LANGUAGE_PATTERNS_QUERY = "select idPattern, name from patternLanguagePattern join pattern on idPattern = pattern.id where idLanguage = ?;";
    private static final String GET_PATTERN_LANGUAGES_QUERY = "select * from patternLanguage;";
    private static final String DELETE_PATTERN_LANGUAGE_PATTERNS_QUERY = "delete from patternLanguagePattern where idLanguage = ?";
    private static final String CREATE_PATTERN_LANGUAGE_PATTERN_QUERY = "insert into patternLanguagePattern(idLanguage, idPattern) values(?,?);";
    private static final String DELETE_PATTERN_LANGUAGE_QUERY = "delete from patternLanguage where id = ?";

    //Failure messages
    private static final String CREATE_PATTERN_FAILED = "Could not create pattern";
    private static final String GET_PATTERNS_FAILED = "Could not select all patterns";

    //Column names
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PATTERN_ID = "idPattern";

    //Database object
    private DatabaseService database;

    /**
     * Constructor
     */
    public MySqlBasedPatternsService()
    {
        database = new MySqlBasedDatabaseService();
    }

    /**
     * Create a pattern
     * @param name The name of the pattern
     * @return The id of the pattern created
     * @throws Exception When the pattern cannot be created
     */
    public int createPattern(String name) throws Exception
    {
        int res;

        Connection con = database.getConnection();

        PreparedStatement stmt = con.prepareStatement(CREATE_PATTERN_QUERY);
        stmt.setString(1, name);
        stmt.execute();

        PreparedStatement stmt2 = con.prepareStatement(GET_LAST_INSERTED_ID);
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

    /**
     * Create a pattern
     * @param id The id of the new pattern
     * @param name The name of the new pattern
     * @return The id of the pattern created
     * @throws Exception When the pattern cannot be created
     */
    public int createPattern(int id, String name) throws Exception
    {
        int res;

        Connection con = database.getConnection();

        PreparedStatement stmt = con.prepareStatement(CREATE_PATTERN_WITH_ID_QUERY);
        stmt.setInt(1, id);
        stmt.setString(2, name);
        stmt.execute();

        PreparedStatement stmt2 = con.prepareStatement(GET_LAST_INSERTED_ID);
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

    /**
     * Get all the patterns
     * @return All the patterns
     * @throws SQLException When an error occurs
     */
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

    /**
     * Get a patter by id
     * @param id The id of the pattern
     * @return The pattern
     * @throws SQLException When an error occurs
     */
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

    /**
     * Rollback pattern database changes
     */
    public void rollbackPatternChanges(){
        rollbackChanges(DELETE_PATTERN_QUERY);
    }

    /**
     * Rollback pattern language database changes
     */
    public void rollbackPatternLanguageChanges(){
        rollbackChanges(DELETE_PATTERN_LANGUAGE_QUERY);
    }

    /**
     * Rollback pattern database changes
     */
    public void rollbackChanges(String query){
        Connection con = database.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(GET_LAST_INSERTED_ID);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                int id = rs.getInt(ID);

                PreparedStatement stmt2 = con.prepareStatement(query);
                stmt2.setInt(1, id);
                stmt2.execute();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        database.closeConnection(con);
    }

    /**
     * Create a pattern language
     * @param name The name of the language
     * @return The id of the language created
     * @throws Exception When an error occured
     */
    public int createPatternLanguage(String name) throws Exception
    {
        int res;

        Connection con = database.getConnection();

        PreparedStatement stmt = con.prepareStatement(CREATE_PATTERN_LANGUAGE_QUERY);
        stmt.setString(1, name);
        stmt.execute();

        PreparedStatement stmt2 = con.prepareStatement(GET_LAST_INSERTED_ID);
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

    /**
     * Get a pattern language by id
     * @param id The id of the pattern language
     * @return The pattern language
     * @throws SQLException When an error occurs
     */
    public PatternLanguage getPatternLanguage(int id) throws SQLException {
        Connection con = database.getConnection();

        PatternLanguage patternLanguage = null;

        PreparedStatement stmt = con.prepareStatement(GET_PATTERN_LANGUAGE_QUERY);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
        {
            patternLanguage = new PatternLanguage(rs.getInt(ID), rs.getString(NAME));

            PreparedStatement stmt2 = con.prepareStatement(GET_PATTERN_LANGUAGE_PATTERNS_QUERY);
            stmt2.setInt(1, id);
            ResultSet rs2 = stmt2.executeQuery();

            while(rs2.next()) {
                patternLanguage.addPattern(new Pattern(rs2.getInt(PATTERN_ID), rs2.getString(NAME)));
            }
        }

        database.closeConnection(con);

        return patternLanguage;
    }

    /**
     * Get all the pattern languages
     * @return All the pattern languages
     * @throws SQLException When an error occured
     */
    public ArrayList<PatternLanguage> getPatternLanguages() throws SQLException {
        Connection con = database.getConnection();

        ArrayList<PatternLanguage> patternLanguages = new ArrayList<>();

        PreparedStatement stmt = con.prepareStatement(GET_PATTERN_LANGUAGES_QUERY);
        ResultSet rs = stmt.executeQuery();

        while(rs.next())
        {
            patternLanguages.add(new PatternLanguage(rs.getInt(ID), rs.getString(NAME)));
        }

        database.closeConnection(con);

        return patternLanguages;
    }

    /**
     * Add patterns to a pattern language
     * @param id The id of the pattern language
     * @param ids The ids of the patterns to use
     * @throws SQLException When an error occured
     */
    public void addPatternLanguagePatterns(int id, ArrayList<Integer> ids) throws SQLException {
        Connection con = database.getConnection();

        PreparedStatement stmt = con.prepareStatement(DELETE_PATTERN_LANGUAGE_PATTERNS_QUERY);
        stmt.setInt(1, id);
        stmt.execute();

        for(Integer i : ids) {
            PreparedStatement stmt2 = con.prepareStatement(CREATE_PATTERN_LANGUAGE_PATTERN_QUERY);
            stmt2.setInt(1, id);
            stmt2.setInt(2, i);
            stmt2.execute();
        }

        database.closeConnection(con);
    }
}
