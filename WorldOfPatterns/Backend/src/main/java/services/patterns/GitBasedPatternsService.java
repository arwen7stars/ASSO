package services.patterns;

import patterns.Pattern;
import services.database.MySqlBasedDatabaseService;
import services.git.CommitBasicInfo;
import services.git.MyGitHubService;
import utils.exceptions.OldRevisionNotFound;
import utils.exceptions.PatternCreationFailedException;
import utils.exceptions.PatternNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static utils.Configs.*;

/**
 * Class that performs Patterns services using GitHub
 */
public class GitBasedPatternsService implements PatternsService{
    private static final String PATTERNS_PATH = "patterns/";
    private static final String PATTERN_CREATION_MESSAGE = "Pattern created";
    private static final String FILE_FORMAT = ".md";

    private MyGitHubService gitHubService;
    private MySqlBasedPatternsService mySqlService;

    /**
     * Constructor. Sets up the GitHub service
     */
    public GitBasedPatternsService() {
        gitHubService = new MyGitHubService(GIT_USER, GIT_PASSWORD, GIT_EMAIL, GIT_REPOSITORY);
        mySqlService = new MySqlBasedPatternsService();
    }

    /**
     * Get all patterns
     * @return List of patterns
     * @throws PatternNotFoundException When the patterns query fails
     */
    public ArrayList<Pattern> getPatterns() throws PatternNotFoundException {
        ArrayList<Pattern> patterns;

        try {
            patterns = mySqlService.getPatterns();
        } catch (SQLException ex) {
            throw new PatternNotFoundException();
        }

        return patterns;
    }

    /**
     * Get a pattern
     * @param id ID of the requested pattern
     * @return The pattern requested
     * @throws PatternNotFoundException When pattern does not exist
     */
    public Pattern getPattern(int id) throws PatternNotFoundException {
        try {
            Pattern pattern = mySqlService.getPattern(id);
            String content = gitHubService.getFileContent(PATTERNS_PATH + id + FILE_FORMAT);

            return generatePattern(id, pattern.getName(), content);
        }
        catch (Exception ex) {
            throw new PatternNotFoundException();
        }
    }

    /**
     * Create a pattern
     * @param name Pattern name
     * @param markdown Pattern markdown
     * @return The created pattern if successful. null otherwise
     * @throws PatternCreationFailedException When pattern already exists
     */
    public Pattern createPattern(String name, String markdown) throws PatternCreationFailedException {
        try {
            System.out.println(name);
            System.out.println(markdown);
            int id = mySqlService.createPattern(name);
            gitHubService.commit(PATTERNS_PATH + id + FILE_FORMAT, markdown, PATTERN_CREATION_MESSAGE);
            return getPattern(id);
        }
        catch(Exception ex) {
            mySqlService.rollbackChanges();
            throw new PatternCreationFailedException();
        }
    }

    /**
     * Update a pattern
     * @param id The name of the pattern to update
     * @param markdown The new markdown
     * @param message The update message
     * @return The updated pattern
     * @throws PatternNotFoundException When the pattern is not found
     */
    public Pattern updatePattern(int id, String markdown, String message) throws PatternNotFoundException {
        try {
            getPattern(id);
            gitHubService.commit(PATTERNS_PATH + id + FILE_FORMAT, markdown, message);
            return getPattern(id);
        } catch (IOException ex) {
            throw new PatternNotFoundException();
        }
    }

    /**
     * Get pattern history
     * @param id The id of the pattern to check history for
     * @return The pattern history as a list of commits with message and date
     * @throws PatternNotFoundException When the pattern does not exist
     */
    public List<CommitBasicInfo> getPatternHistory(int id) throws PatternNotFoundException {
        try {
            List<CommitBasicInfo> res = gitHubService.getRepositoryCommits(PATTERNS_PATH + id + FILE_FORMAT);

            if(res.size() == 0) {
                throw new PatternNotFoundException();
            }

            return res;
        }
        catch(IOException ex) {
            throw new PatternNotFoundException();
        }
    }

    /**
     * Get pattern old revision
     * @param id The id of the pattern
     * @param sha The sha ref of the pattern
     * @return The old revision of the pattern
     * @throws OldRevisionNotFound When the the sha is invalid
     * @throws PatternNotFoundException When the pattern does not exist
     */
    public Pattern getPatternOldRevision(int id, String sha) throws OldRevisionNotFound, PatternNotFoundException {
        try {
            Pattern pattern = getPattern(id);

            String content = gitHubService.getOldFileRevisionContent(PATTERNS_PATH + id + FILE_FORMAT, sha);

            return generatePattern(id, pattern.getName(), content);
        }
        catch (IOException ex) {
            throw new OldRevisionNotFound();
        }
    }

    /**
     * Generate a pattern
     * @param name The name of the pattern to generate
     * @param content The content of the pattern to generate
     * @return The pattern with markdown and html
     * @throws IOException When the content does not exist or no html can be obtained
     */
    public Pattern generatePattern(int id, String name, String content) throws IOException {
        if(content != null) {
            String html = gitHubService.getHtmlFromMarkdown(content);

            if (html != null) {
                return new Pattern(id, name, html, content);
            } else {
                throw new IOException();
            }
        }
        else {
            throw new IOException();
        }
    }
}
