package services.patterns;

import patterns.Pattern;
import patterns.PatternLanguage;
import services.git.CommitBasicInfo;
import services.git.GitFileBasicInfo;
import services.git.MyGitHubService;
import utils.exceptions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static utils.Configs.*;
import static utils.Utils.*;

/**
 * Class that performs Patterns services using GitHub
 */
public class GitBasedPatternsService implements PatternsService{
    private static final String PATTERNS_PATH = "patterns/";
    private static final String PATTERN_CREATION_MESSAGE = "Pattern created";

    private static final String START_LOADING = "Started loading";
    private static final String FINISH_LOADING = "Finished loading";

    private MyGitHubService gitHubService;
    private MySqlBasedPatternsService mySqlService;

    /**
     * Constructor. Sets up the GitHub service
     */
    public GitBasedPatternsService() {
        gitHubService = new MyGitHubService(GIT_USER, GIT_PASSWORD, GIT_EMAIL, GIT_REPOSITORY);
        mySqlService = new MySqlBasedPatternsService();
        if(PULL_FROM_GIT) {
            gatherPatterns();
        }
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
            String url = encodeUrl(PATTERNS_PATH + id + SEPARATOR + pattern.getName() + FILE_FORMAT);
            String content = gitHubService.getFileContent(url);

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
            int id = mySqlService.createPattern(name);
            String url = encodeUrl(PATTERNS_PATH + id + SEPARATOR + name + FILE_FORMAT);
            gitHubService.commit(url, markdown, PATTERN_CREATION_MESSAGE);
            return getPattern(id);
        }
        catch(Exception ex) {
            mySqlService.rollbackPatternChanges();
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
            Pattern pattern = getPattern(id);
            String url = encodeUrl(PATTERNS_PATH + id + SEPARATOR + pattern.getName() + FILE_FORMAT);
            gitHubService.commit(url, markdown, message);
            mySqlService.updatePattern(id);
            return getPattern(id);
        } catch (IOException | SQLException ex) {
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
            Pattern pattern = mySqlService.getPattern(id);

            List<CommitBasicInfo> res = gitHubService.getRepositoryCommits(PATTERNS_PATH + id + SEPARATOR + pattern.getName() + FILE_FORMAT);

            if(res.size() == 0) {
                throw new PatternNotFoundException();
            }

            return res;
        }
        catch(Exception ex) {
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
            String url = encodeUrl(PATTERNS_PATH + id + SEPARATOR + pattern.getName() + FILE_FORMAT);
            String content = gitHubService.getOldFileRevisionContent(url, sha);

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

    /**
     * Pull patterns from repository
     */
    public void gatherPatterns() {
        System.out.println(START_LOADING);

        try {
            ArrayList<GitFileBasicInfo> res = gitHubService.getRepositoryContents(PATTERNS_PATH);
            for(GitFileBasicInfo info : res) {
                try {
                    mySqlService.createPattern(info.getId(), info.getName(), info.getDate());
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        System.out.println(FINISH_LOADING);
    }

    /**
     * Encode a url string from a base string
     * @param string The initial string to encode
     * @return The encoded result
     */
    public String encodeUrl(String string) {
        return string.replace(SPACE, SPACE_ENCODED);
    }

    /**
     * Create a pattern language
     * @param name The name of the pattern language
     * @param ids The pattern ids
     * @return The pattern language created
     * @throws PatternLanguageCreationFailedException When the pattern language creation failed
     */
    public PatternLanguage createPatternLanguage(String name, ArrayList<Integer> ids) throws PatternLanguageCreationFailedException {
        try {
            int id = mySqlService.createPatternLanguage(name);
            mySqlService.addPatternLanguagePatterns(id, ids);
            return mySqlService.getPatternLanguage(id);
        }
        catch(Exception ex) {
            mySqlService.rollbackPatternLanguageChanges();
            throw new PatternLanguageCreationFailedException();
        }
    }

    /**
     * Get a pattern language
     * @param id The id of the pattern language
     * @return The pattern language
     * @throws PatternLanguageNotFoundException When the pattern language is not found
     */
    public PatternLanguage getPatternLanguage(int id) throws PatternLanguageNotFoundException {
        try {
            return mySqlService.getPatternLanguage(id);
        }
        catch (Exception ex) {
            throw new PatternLanguageNotFoundException();
        }
    }

    /**
     * Get all the pattern languages
     * @return All the pattern languages
     * @throws PatternLanguageNotFoundException When an error occurs
     */
    public ArrayList<PatternLanguage> getPatternLanguages() throws PatternLanguageNotFoundException {
        try {
            return mySqlService.getPatternLanguages();
        }
        catch (Exception ex) {
            throw new PatternLanguageNotFoundException();
        }
    }

    /**
     * Update a pattern language
     * @param id The id of the pattern language
     * @param ids The ids of the patterns to use
     * @return The new pattern language
     * @throws PatternLanguageNotFoundException When the pattern language is not found
     */
    public PatternLanguage updatePatternLanguage(int id, ArrayList<Integer> ids) throws PatternLanguageNotFoundException {
        try {
            mySqlService.addPatternLanguagePatterns(id, ids);
            return mySqlService.getPatternLanguage(id);
        }
        catch(Exception ex) {
            throw new PatternLanguageNotFoundException();
        }
    }

    /**
     * Search patterns
     * @param query The keyword to look for
     * @return A list of patterns that contain the query
     * @throws PatternLanguageNotFoundException When an error occurs
     */
    public ArrayList<Pattern> searchPatterns(String query) throws SearchFailedException {
        ArrayList<Pattern> patterns = new ArrayList<>();

        try {
            ArrayList<String> results = gitHubService.searchRepository(query);

            for(String s : results) {
                String[] split = s.split(SEPARATOR);

                if(split.length == 3) {
                    patterns.add(new Pattern(Integer.parseInt(split[1]), split[2].replace(FILE_FORMAT, EMPTY_STRING).replace(DOUBLE_QUOTE, EMPTY_STRING)));
                }
            }
        }
        catch (IOException ex) {
            throw new SearchFailedException();
        }

        return patterns;
    }
}
